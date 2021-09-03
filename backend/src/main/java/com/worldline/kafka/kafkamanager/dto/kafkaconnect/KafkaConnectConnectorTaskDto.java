package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Kafka connect task.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KafkaConnectConnectorTaskDto extends KafkaConnectTaskIdDto implements Serializable {

	private static final long serialVersionUID = 1342833092026476727L;

	private String id;

	private String state;

	@JsonProperty("worker_id")
	private String workerId;

	private String trace;

}
