package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Kafka connect connector.
 */
@Data
public class KafkaConnectConnectorUpdateDto implements Serializable {

	private static final long serialVersionUID = 5888933935472380156L;

	@NotNull
	private Map<String, String> config;

}
