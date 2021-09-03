package com.worldline.kafka.kafkamanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Bad request exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends KafkaManagerException {

	private static final long serialVersionUID = -8362377023048776501L;

	public BadRequestException(String message) {
		super(message);
	}

}
