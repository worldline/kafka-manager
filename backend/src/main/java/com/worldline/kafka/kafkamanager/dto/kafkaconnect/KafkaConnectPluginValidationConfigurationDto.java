package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import lombok.Data;

/**
 * Kafka connect plugin validation configuration.
 */
@Data
public class KafkaConnectPluginValidationConfigurationDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	private KafkaConnectPluginValidationDefinitionDto definition;

	private KafkaConnectPluginValidationValueDto value;

}
