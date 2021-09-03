package com.worldline.kafka.kafkamanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Resource not found exception.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends KafkaManagerException {

	private static final long serialVersionUID = -8362377023048776501L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
