package com.worldline.kafka.kafkamanager.mapper;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescriptionDto;
import com.worldline.kafka.kafkamanager.kafka.KafkaOffsets;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.common.acl.AclOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Consumer group mapper.
 */
@Service
public class ConsumerGroupMapper {

	private final MemberMapper memberMapper;
	private final KafkaNodeMapper kafkaNodeMapper;

	/**
	 * Constructor.
	 * 
	 * @param memberMapper    the member mapper
	 * @param kafkaNodeMapper the kafka node mapper
	 */
	@Autowired
	public ConsumerGroupMapper(MemberMapper memberMapper, KafkaNodeMapper kafkaNodeMapper) {
		this.memberMapper = memberMapper;
		this.kafkaNodeMapper = kafkaNodeMapper;
	}

	/**
	 * Map {@link ConsumerGroupDescription} to {@link ConsumerGroupDescriptionDto}.
	 * 
	 * @param raw     {@link ConsumerGroupDescription}
	 * @param offsets the offset data
	 * @return {@link ConsumerGroupDescriptionDto}
	 */
	public ConsumerGroupDescriptionDto map(ConsumerGroupDescription raw, KafkaOffsets offsets) {
		ConsumerGroupDescriptionDto.ConsumerGroupDescriptionDtoBuilder builder = ConsumerGroupDescriptionDto.builder()
				.groupId(raw.groupId()).isSimpleConsumerGroup(raw.isSimpleConsumerGroup())
				.members(raw.members().stream().map(m -> memberMapper.mapFromNative(m, raw.groupId(), offsets))
						.collect(Collectors.toList()))
				.partitionAssignor(raw.partitionAssignor()).state(raw.state() != null ? raw.state().name() : null)
				.coordinator(kafkaNodeMapper.map(raw.coordinator()));

		if (CollectionUtils.isNotEmpty(raw.authorizedOperations())) {
			builder.authorizedOperations(
					raw.authorizedOperations().stream().map(AclOperation::name).collect(Collectors.toSet()));
		}
		return builder.build();
	}
}
