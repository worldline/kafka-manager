package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Kafka connect plugin vallidation response.
 */
@Data
public class KafkaConnectPluginValidationResponseDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	private String name;

	@JsonProperty("error_count")
	private int errorCount;

	private List<String> groups;

	private List<KafkaConnectPluginValidationConfigurationDto> configs;

}
