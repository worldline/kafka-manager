package com.worldline.kafka.kafkamanager.dto.kafka.common;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDescriptionDto implements Serializable {

	private static final long serialVersionUID = -6013480839542818481L;

	private String groupId;
	private String consumerId;
	private Optional<String> groupInstanceId;
	private String clientId;
	private String host;
	private Set<ConsumerGroupTopicPartitionDto> topicPartitions;

}
