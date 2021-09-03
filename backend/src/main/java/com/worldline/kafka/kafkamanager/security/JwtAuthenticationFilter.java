package com.worldline.kafka.kafkamanager.security;

import com.worldline.kafka.kafkamanager.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final static String TOKEN_HEADER = "Authorization";
	private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String token = request.getHeader(TOKEN_HEADER);
		if (token != null) {

			try {
				Claims claims = jwtService.getClaimsFromToken(token.replace("Bearer ", ""));

				Authentication authentication = new PreAuthenticatedAuthenticationToken(
					claims.get("firstName"),
					null,
					getAuthoritiesFromClaims(claims)
				);

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (ExpiredJwtException exception) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT is expired");
				return;
			} catch (JwtException exception) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
				return;
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	private List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
		//noinspection unchecked
		return ((List<String>) claims.get("roles")).stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

}
