package com.worldline.kafka.kafkamanager.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenIdTokenDto {
	
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private int expiresIn;

	@JsonProperty("not-before-policy")
	private boolean notBeforePolicy;
	
	@JsonProperty("refresh_expires_in")
	private int refresh_expires_in;

	@JsonProperty("refresh_token")
	private String refreshToken;
	
	private String scope;

	@JsonProperty("session_state")
	private String sessionState;

	@JsonProperty("token_type")
	private String tokenType;
}
