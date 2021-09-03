package com.worldline.kafka.kafkamanager.dto.consumergroupsoffset;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;
import com.worldline.kafka.kafkamanager.dto.kafka.common.OffsetAndMetadataDto;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ConsumerGroupOffsetDto {
	
	public String groupId;
	public Map<ConsumerGroupTopicPartitionDto, OffsetAndMetadataDto> topicsAndOffsets;
	
	public ConsumerGroupOffsetDto(String groupId) {
		this.groupId = groupId;
		this.topicsAndOffsets = new HashMap<>();
	}

	public void add(ConsumerGroupTopicPartitionDto topicPartitionDto, OffsetAndMetadataDto offsetAndMetadataDto) {
		this.topicsAndOffsets.put(topicPartitionDto, offsetAndMetadataDto);
	}
}
