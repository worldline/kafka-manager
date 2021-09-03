package com.worldline.kafka.kafkamanager.dto.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;

import lombok.Data;

/**
 * Contains a map of {@link ConsumerRecordDto} (data from topic) per partition number.
 */
@Data
public class ConsumerRecordsResponseDto {
	
	private Map<Integer, List<ConsumerRecordDto>> records;
	
	public ConsumerRecordsResponseDto() {
		this.records = new HashMap<>();
	}

	/**
	 * Add a new record in the for the {@link ConsumerGroupTopicPartitionDto}.
	 * @param partitionNb Topic partition
	 * @param recordDto Record to save
	 */
	public void addRecordForPartition(Integer partitionNb, ConsumerRecordDto recordDto) {
		
		if (records.containsKey(partitionNb)) {
			records.get(partitionNb).add(recordDto);
		} else {
			records.put(partitionNb, new ArrayList<>());
			records.get(partitionNb).add(recordDto);
		}
	}

}
