package com.worldline.kafka.kafkamanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Zookeeper exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ZookeeperException extends KafkaManagerException {

	private static final long serialVersionUID = -8362377023048776501L;

	public ZookeeperException(String message) {
		super(message);
	}

	public ZookeeperException(String message, Throwable t) {
		super(message, t);
	}

}
