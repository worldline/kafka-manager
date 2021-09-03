package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Kafka connect topics.
 */
@Data
public class KafkaConnectTopicsDto implements Serializable {

	private static final long serialVersionUID = 4498866674728987760L;

	private List<String> topics;

}
