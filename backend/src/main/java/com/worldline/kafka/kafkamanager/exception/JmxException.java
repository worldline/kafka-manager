package com.worldline.kafka.kafkamanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * JMX exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JmxException extends KafkaManagerException {

	private static final long serialVersionUID = -8362377023048776501L;

	public JmxException(String message) {
		super(message);
	}

	public JmxException(String message, Throwable t) {
		super(message, t);
	}

}
