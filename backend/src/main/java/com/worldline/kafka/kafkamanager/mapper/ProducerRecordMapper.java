package com.worldline.kafka.kafkamanager.mapper;

import com.worldline.kafka.kafkamanager.dto.producteur.ProducerRecordDto;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

@Service
public class ProducerRecordMapper {

	/**
	 * Conver {@link RecordMetadata} to {@link ProducerRecordDto}. {@link RecordMetadata} contains  results from a message
	 * pushed to a topic.
	 * @param metadata Kafka native object
	 */
	public ProducerRecordDto map(RecordMetadata metadata) {
		ProducerRecordDto record = new ProducerRecordDto();
		record.setPartition(metadata.partition());
		record.setOffset(metadata.offset());
		record.setTimestamp(metadata.timestamp());
		return record;
	}

}
