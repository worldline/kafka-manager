package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Kafka connect connector.
 */
@Data
public class KafkaConnectConnectorDto implements Serializable {

	private static final long serialVersionUID = 5888933935472380156L;

	@NotBlank
	private String name;

	private Map<String, String> config;

	private List<@Valid KafkaConnectConnectorTaskDto> tasks;

	private String type;

	private Map<String, String> connector;

	private KafkaConnectorStatus status = KafkaConnectorStatus.UP;

}
