package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Kafka connect plugin validation definition.
 */
@Data
public class KafkaConnectPluginValidationDefinitionDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	private String name;

	private String type;

	private boolean required;

	@JsonProperty("default_value")
	private String defaultValue;

	private String importance;

	private String documentation;

	private String group;

	private String width;

	@JsonProperty("display_name")
	private String displayName;

	private int order;

}
