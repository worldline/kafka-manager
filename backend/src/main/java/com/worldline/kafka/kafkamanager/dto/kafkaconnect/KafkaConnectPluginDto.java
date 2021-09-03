package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Kafka connect plugin.
 */
@Data
public class KafkaConnectPluginDto implements Serializable {

	private static final long serialVersionUID = -6231337677670581768L;

	@JsonProperty("class")
	private String pluginClass;

	private String type;

	private String version;

}
