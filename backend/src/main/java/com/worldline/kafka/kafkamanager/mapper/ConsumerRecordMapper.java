package com.worldline.kafka.kafkamanager.mapper;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.cloud.stream.binder.EmbeddedHeaderUtils;
import org.springframework.cloud.stream.binder.MessageValues;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerRecordDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer record mapper.
 */
@Slf4j
@Service
public class ConsumerRecordMapper {

	/**
	 * Map {@link ConsumerRecord} to {@link ConsumerRecordDto}.
	 * 
	 * @param rawRecord {@link ConsumerRecord}
	 * @return {@link ConsumerRecordDto}
	 */
	public ConsumerRecordDto map(ConsumerRecord<byte[], byte[]> rawRecord) {
		ConsumerRecordDto record = new ConsumerRecordDto();
		record = mapRecordValue(record, rawRecord);
		if (rawRecord.key() != null) {
			record.setKey(new String(rawRecord.key(), StandardCharsets.UTF_8));
		}
		record.setOffset(rawRecord.offset());
		record.setTimestamp(rawRecord.timestamp());
		record.setPartition(rawRecord.partition());
		return record;
	}

	/**
	 * Map value from {@link ConsumerRecord} to {@link ConsumerRecordDto}.
	 * 
	 * @param record    {@link ConsumerRecordDto}
	 * @param rawRecord {@link ConsumerRecord}
	 * @return {@link ConsumerRecordDto}
	 */
	private ConsumerRecordDto mapRecordValue(ConsumerRecordDto record, ConsumerRecord<byte[], byte[]> rawRecord) {
		// Set value
		record.setValue(new String(rawRecord.value(), StandardCharsets.UTF_8));
		try {
			MessageValues messageValues = EmbeddedHeaderUtils.extractHeaders(rawRecord.value());
			record.setValue(new String((byte[]) messageValues.getPayload(), StandardCharsets.UTF_8));
			if (messageValues.getHeaders() != null) {
				messageValues.getHeaders().entrySet().stream().filter(e -> e.getValue() != null)
						.forEach(e -> record.addHeader(e.getKey(), e.getValue().toString()));
			}
		} catch (Exception exception) {
			log.error("Cannot get headers", exception);
		}

		// Set headers
		Headers headers = rawRecord.headers();
		if (headers != null) {
			headers.forEach(
					header -> record.addHeader(header.key(), new String(header.value(), StandardCharsets.UTF_8)));
		}
		return record;
	}

}
