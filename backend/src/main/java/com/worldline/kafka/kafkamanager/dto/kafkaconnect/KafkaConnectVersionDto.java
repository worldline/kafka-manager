package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Kafka connect version.
 */
@Data
public class KafkaConnectVersionDto implements Serializable {

	private static final long serialVersionUID = -8399508196086935466L;

	private String version;

	private String commit;

	@JsonProperty("kafka_cluster_id")
	private String clusterId;

}
