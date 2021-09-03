package com.worldline.kafka.kafkamanager.dto.topic;

import com.worldline.kafka.kafkamanager.dto.partition.PartitionResponseDto;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Topic response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicResponseDto extends TopicAbstractDto implements Serializable {

	private static final long serialVersionUID = 4892886094786822741L;

	private Integer nbReplications;

	private Integer nbUnderReplication;

	private Integer nbBrokerForTopic;

	private Long sumPartitionOffset;

	private Integer preferredReplicas;

	private Integer brokersSpread;

	private Long brokersLeaderSkewed;

	private Long brokersSkewed;

	private Integer totalNumberOfBrokers;

	private List<PartitionResponseDto> partitions;

	private List<TopicBrokerResponseDto> brokers;
}
