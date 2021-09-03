package com.worldline.kafka.kafkamanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Kafka exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConnectionException extends KafkaManagerException {

	private static final long serialVersionUID = -8362377023048776501L;

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(String message, Throwable t) {
		super(message, t);
	}

}
