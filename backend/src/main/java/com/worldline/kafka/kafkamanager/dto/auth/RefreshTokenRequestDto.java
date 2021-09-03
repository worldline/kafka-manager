package com.worldline.kafka.kafkamanager.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequestDto {

	@NotBlank
	private String token;

}
