package com.worldline.kafka.kafkamanager.mapper;

import com.worldline.kafka.kafkamanager.dto.kafka.common.KafkaNodeDto;
import org.apache.kafka.common.Node;
import org.springframework.stereotype.Service;

/**
 * Kafka node mapper.
 */
@Service
public class KafkaNodeMapper {

	/**
	 * Map {@link Node} to {@link KafkaNodeDto}.
	 * 
	 * @param raw  Object to map
	 * @return Mapped object
	 */
	public KafkaNodeDto map(Node raw) {
		return KafkaNodeDto.builder()
			.id(raw.idString())
			.host(raw.host())
			.port(raw.port())
			.rack(raw.rack())
			.build();
	}
}
