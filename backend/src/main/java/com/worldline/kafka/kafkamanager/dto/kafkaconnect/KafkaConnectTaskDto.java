package com.worldline.kafka.kafkamanager.dto.kafkaconnect;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * Kafka connect task dto.
 */
@Data
public class KafkaConnectTaskDto implements Serializable {

	private static final long serialVersionUID = 2899887265548763780L;

	private KafkaConnectTaskIdDto id;

	private Map<String, String> config;

}
