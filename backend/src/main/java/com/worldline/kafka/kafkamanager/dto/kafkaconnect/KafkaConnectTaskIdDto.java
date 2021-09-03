package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import lombok.Data;

/**
 * Kafka connect task id dto.
 */
@Data
public class KafkaConnectTaskIdDto implements Serializable {

	private static final long serialVersionUID = 2934675072346284102L;

	private String connector;

	private int task;

}
