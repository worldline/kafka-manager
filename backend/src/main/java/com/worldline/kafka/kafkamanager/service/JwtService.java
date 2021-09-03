package com.worldline.kafka.kafkamanager.service;

import com.worldline.kafka.kafkamanager.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

	private final Long expiration;
	private final SecretKey key;

	public JwtService(JwtProperties jwtProperties) {
		this.expiration = jwtProperties.getExpiration();
		this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecret()));
	}

	public String generateToken(String username, Map<String, Object> claims) {

		return Jwts.builder()
			.setId(UUID.randomUUID().toString())
			.setSubject(username)
			.setClaims(claims)
			.setIssuedAt(new Date())
			.setExpiration(generateExpirationDate())
			.signWith(key)
			.compact();
	}

	public Claims getClaimsFromToken(String token) {

		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public Claims getClaimsFromOpenIdToken(String token) {

		JwtParser parser = Jwts.parserBuilder()
			.build();

		int unsignedDelimiter = token.lastIndexOf('.');
		String unsignedToken = token.substring(0, unsignedDelimiter + 1);

		return parser.parseClaimsJwt(unsignedToken).getBody();
	}

	public String refreshToken(String token) {

		Claims claims = this.getClaimsFromToken(token);

		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(generateExpirationDate())
			.signWith(key)
			.compact();
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

}
