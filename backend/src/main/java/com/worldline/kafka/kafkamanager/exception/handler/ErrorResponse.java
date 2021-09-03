package com.worldline.kafka.kafkamanager.exception.handler;

import lombok.Getter;

import java.io.Serializable;

public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = 7579767431175359584L;

	@Getter
	private final String message;

	public ErrorResponse(String message) {
		this.message = message;
	}

}
