package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Kafka connect task status.
 */
@Data
public class KafkaConnectTaskStatusDto implements Serializable {

	private static final long serialVersionUID = 1342833092026476727L;

	private String state;

	@JsonProperty("worker_id")
	private String workerId;

	private String trace;

}
