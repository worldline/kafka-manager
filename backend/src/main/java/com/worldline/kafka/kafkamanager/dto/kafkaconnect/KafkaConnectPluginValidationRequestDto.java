package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Kafka connect plugin validation request.
 */
@Data
public class KafkaConnectPluginValidationRequestDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	@NotNull
	private Map<String, String> config;

}
