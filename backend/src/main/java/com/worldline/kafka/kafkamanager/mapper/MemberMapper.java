package com.worldline.kafka.kafkamanager.mapper;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.MemberDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerOffsetDto;
import com.worldline.kafka.kafkamanager.dto.kafka.common.MemberDescriptionDto;
import com.worldline.kafka.kafkamanager.kafka.KafkaOffset;
import com.worldline.kafka.kafkamanager.kafka.KafkaOffsets;

/**
 * Member mapper.
 */
@Service
public class MemberMapper {

	private TopicMapper topicMapper;

	/**
	 * Constructor.
	 * 
	 * @param topicMapper the topic mapper
	 */
	@Autowired
	public MemberMapper(TopicMapper topicMapper) {
		this.topicMapper = topicMapper;
	}

	/**
	 * Map {@link MemberDescription} to {@link MemberDescriptionDto}.
	 * 
	 * @param raw     {@link MemberDescription}
	 * @param groupId the group ID
	 * @param offsets the offset data
	 * @return {@link MemberDescriptionDto}
	 */
	public MemberDescriptionDto mapFromNative(MemberDescription raw, String groupId, KafkaOffsets offsets) {
		MemberDescriptionDto.MemberDescriptionDtoBuilder builder = MemberDescriptionDto.builder()
				.consumerId(raw.consumerId()).groupInstanceId(raw.groupInstanceId()).clientId(raw.clientId())
				.host(raw.host()).groupId(groupId);

		if (raw.assignment() != null && CollectionUtils.isNotEmpty(raw.assignment().topicPartitions())) {
			builder.topicPartitions(raw.assignment().topicPartitions().stream().map(t -> {
				ConsumerOffsetDto partitionOffset = null;
				Optional<KafkaOffset> offset = offsets.get(t.topic(), t.partition(), groupId);
				if (offset.isPresent()) {
					partitionOffset = new ConsumerOffsetDto();
					partitionOffset.setCurrent(offset.get().getOffset());
					partitionOffset.setEnd(offset.get().getEnd());
				}
				return topicMapper.mapFromKafkaNative(t, partitionOffset);
			}).collect(Collectors.toSet()));
		}
		return builder.build();
	}
}
