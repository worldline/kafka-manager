package com.worldline.kafka.kafkamanager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldline.kafka.kafkamanager.dto.auth.AuthenticationRequestDto;
import com.worldline.kafka.kafkamanager.dto.auth.AuthenticationResponseDto;
import com.worldline.kafka.kafkamanager.dto.auth.OpenIdTokenDto;
import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.exception.OpenIdException;
import com.worldline.kafka.kafkamanager.properties.InMemoryAuthenticationProperties;
import com.worldline.kafka.kafkamanager.properties.OpenIdProperties;
import com.worldline.kafka.kafkamanager.service.JwtService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

import io.jsonwebtoken.Claims;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Authentication controller.
 */
@RestController
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final OpenIdProperties openIdProperties;
	private final JwtService jwtService;
	private final InMemoryAuthenticationProperties inMemoryAuthenticationProperties;

	/**
	 * Controller.
	 *
	 * @param authenticationManager            the athentication manager
	 * @param openIdProperties                 the open ID properties
	 * @param jwtService                       the jwt service
	 * @param inMemoryAuthenticationProperties the in memory properties
	 */
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, OpenIdProperties openIdProperties,
			JwtService jwtService, InMemoryAuthenticationProperties inMemoryAuthenticationProperties) {
		this.authenticationManager = authenticationManager;
		this.openIdProperties = openIdProperties;
		this.jwtService = jwtService;
		this.inMemoryAuthenticationProperties = inMemoryAuthenticationProperties;
	}

	/**
	 * Login entry point.
	 *
	 * @param authenticationRequest the authentication request
	 * @return the authentication data
	 */
	@PostMapping(value = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@ActivityEvent(value = EventType.LOGIN, args = {
			@ActivityEventArg(key = EventArgs.USER, value = "#authenticationRequest.username") })
	public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) throws IOException {
		if (inMemoryAuthenticationProperties.isEnabled()) {
			return authenticateInMemory(authenticationRequest);
		}
		return oauth2Login(authenticationRequest);
	}

	@GetMapping("/refresh")
	public AuthenticationResponseDto refreshToken(@RequestHeader("Authorization") String token) {
		return new AuthenticationResponseDto(jwtService.refreshToken(token.replace("Bearer ", "")));
	}

	/**
	 * Authentication in memory.
	 *
	 * @param authenticationRequest the authentication request
	 * @return the authentication data
	 */
	private AuthenticationResponseDto authenticateInMemory(AuthenticationRequestDto authenticationRequest) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles);
		claims.put("firstName", authentication.getName());

		return new AuthenticationResponseDto(jwtService.generateToken(authentication.getName(), claims));
	}

	/**
	 * Authentication with oauth2.
	 *
	 * @param authenticationRequest the authentication request
	 * @return the authentication data
	 */
	private AuthenticationResponseDto oauth2Login(AuthenticationRequestDto authenticationRequest) throws IOException {

		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		String body = "grant_type=password&client_id=" + openIdProperties.getClientId() + "&client_secret="
				+ openIdProperties.getSecret() + "&username=" + authenticationRequest.getUsername().toUpperCase()
				+ "&password=" + authenticationRequest.getPassword();

		RequestBody requestBody = RequestBody.create(mediaType, body);

		Request request = new Request.Builder().url(openIdProperties.getUrl()).post(requestBody)
				.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.header("User-Agent", "KafkaManager").build();

		Response response = client.newCall(request).execute();

		if (HttpStatus.OK.value() != response.code()) {
			throw new OpenIdException(response.body().string());
		}

		ObjectMapper objectMapper = new ObjectMapper();
		OpenIdTokenDto openIdToken = objectMapper.readValue(response.body().bytes(), OpenIdTokenDto.class);

		Claims openIdClaims = jwtService.getClaimsFromOpenIdToken(openIdToken.getAccessToken());

		// noinspection unchecked
		Map<String, Map<String, List<String>>> access = (Map<String, Map<String, List<String>>>) openIdClaims
				.get("resource_access");
		String userName = (String) openIdClaims.get("preferred_username");

		List<String> roles = new ArrayList<>();

		if (isAdmin(userName, access.get("account").get("roles"))) {
			roles.add("ADMIN");
		} else {
			roles.add("USER");
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles);
		claims.put("firstName", openIdClaims.get("given_name"));

		return new AuthenticationResponseDto(jwtService.generateToken(userName, claims));
	}
	
	/**
	 * Check if the connected user is an admin.
	 * @param username the user login
	 * @param roles the user roles
	 * @return {@code true} if the connected user is an admin
	 */
	private boolean isAdmin(String username, List<String> roles) {
		return (roles.stream().anyMatch(openIdProperties.getAdminRoleMapping()::equals)
				&& (CollectionUtils.isEmpty(openIdProperties.getAdminUsernameMapping())
			|| openIdProperties.getAdminUsernameMapping().stream().anyMatch(username::equals)))
			|| openIdProperties.getAdminWhiteList().stream().anyMatch(username::equals);
	}

}
