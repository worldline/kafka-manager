package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Kafka connect plugin validation value.
 */
@Data
public class KafkaConnectPluginValidationValueDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	private String name;

	private String value;

	private List<String> errors;

	private boolean visible;
}
