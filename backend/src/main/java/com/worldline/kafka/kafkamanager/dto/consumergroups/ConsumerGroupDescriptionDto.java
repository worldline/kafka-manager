package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.worldline.kafka.kafkamanager.dto.kafka.common.KafkaNodeDto;
import com.worldline.kafka.kafkamanager.dto.kafka.common.MemberDescriptionDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumerGroupDescriptionDto implements Serializable {

	private static final long serialVersionUID = 6283031732359933773L;

	private String groupId;
	private boolean isSimpleConsumerGroup;
	private String state;

	private List<MemberDescriptionDto> members;
	private String partitionAssignor;
	private KafkaNodeDto coordinator;
	private Set<String> authorizedOperations;
}
