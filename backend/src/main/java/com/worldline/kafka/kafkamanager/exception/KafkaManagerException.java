package com.worldline.kafka.kafkamanager.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Kafka exception.
 */
@Slf4j
abstract public class KafkaManagerException extends RuntimeException {

	private static final long serialVersionUID = -8362377023048776501L;

	public KafkaManagerException(String message) {
		super(message);
	}

	public KafkaManagerException(String message, Throwable t) {
		super(message, t);
		log.error("", t);
	}

}
