package com.worldline.kafka.kafkamanager.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {

	@NotBlank
	private String password;

	@NotBlank
	private String username;

}
