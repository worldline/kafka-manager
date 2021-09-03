package com.worldline.kafka.kafkamanager.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupOffsetPartitionResetDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupOffsetResetDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;
import com.worldline.kafka.kafkamanager.dto.consumergroupsoffset.ConsumerGroupOffsetDto;
import com.worldline.kafka.kafkamanager.dto.kafka.common.OffsetAndMetadataDto;
import com.worldline.kafka.kafkamanager.exception.KafkaException;
import com.worldline.kafka.kafkamanager.kafka.KafkaOffsets;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.OffsetAndMetadataMapper;
import com.worldline.kafka.kafkamanager.mapper.TopicMapper;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumerGroupOffsetService {

	private final ZooKaMixService zooKaMixService;
	private final TopicMapper topicMapper;
	private final OffsetAndMetadataMapper offsetAndMetadataMapper;

	/**
	 * Constructor.
	 *
	 * @param zooKaMixService         the zookeeper kafka jmx service
	 * @param topicMapper             the topic mapper
	 * @param offsetAndMetadataMapper the offset and metadata mapper
	 */
	@Autowired
	public ConsumerGroupOffsetService(ZooKaMixService zooKaMixService, TopicMapper topicMapper,
			OffsetAndMetadataMapper offsetAndMetadataMapper) {

		this.zooKaMixService = zooKaMixService;
		this.topicMapper = topicMapper;
		this.offsetAndMetadataMapper = offsetAndMetadataMapper;
	}

	/**
	 * Get offsets.
	 *
	 * @param zooKaMixAdmin the connection
	 * @param topicName     the topic name
	 * @param partitions    the partition list
	 * @return the offsets
	 */
	public Map<Integer, Long> getEndOffset(ZooKaMixAdmin zooKaMixAdmin, String topicName, Collection<Integer> partitions) {
		
		if(CollectionUtils.isEmpty(partitions)) {
			return new HashMap<>();
		}
		
		// Create partitions
		List<TopicPartition> topicPartitions = partitions.stream()
			.map(partition -> new TopicPartition(topicName, partition))
			.collect(Collectors.toList());

		// Create kafka consumer
		try (KafkaConsumer<Byte[], Byte[]> consumer = zooKaMixAdmin.createConsumer(Byte[].class)) {
			consumer.assign(topicPartitions);

			// Get end offsets
			Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions, Duration.ofSeconds(5));
			Map<Integer, Long> result = endOffsets.entrySet().stream().collect(Collectors.toMap(e -> {
				return e.getKey().partition();
			}, e -> {
				return e.getValue();
			}));

			consumer.unsubscribe();
			return result;
		}
	}

	/**
	 * Get offsets.
	 *
	 * @param zooKaMixAdmin  the connection
	 * @param consumerGroups the consumer group data
	 * @return the offsets
	 */
	public KafkaOffsets getOffsets(ZooKaMixAdmin zooKaMixAdmin, Map<String, ConsumerGroupDescription> consumerGroups) {
		// Get topics and partitions
		Map<String, Set<Integer>> topicPartitions = new HashMap<>();
		consumerGroups.values().stream().forEach(consumerGroup -> {
			consumerGroup.members().stream().forEach(member -> {
				member.assignment().topicPartitions().stream().forEach(partition -> {
					if (!topicPartitions.containsKey(partition.topic())) {
						topicPartitions.put(partition.topic(), new HashSet<>());
					}
					topicPartitions.get(partition.topic()).add(partition.partition());
				});
			});
		});

		// Get current offsets
		KafkaOffsets offsets = zooKaMixAdmin.getCurrentOffset(consumerGroups);

		// Map data
		offsets.getByTopic().entrySet().stream().forEach(e -> {
			// Get end offsets
			String topic = e.getKey();
			try {
				Map<Integer, Long> endOffset = getEndOffset(zooKaMixAdmin, topic, topicPartitions.get(topic));
				e.getValue().stream().forEach(offset -> {
					if(endOffset.containsKey(offset.getPartition())) {
						offset.setEnd(endOffset.get(offset.getPartition()));	
					}
				});
			} catch (TimeoutException exception) {
				log.error("Kafka timeout", exception);
			}
		});

		return offsets;
	}

	/**
	 * Get the detail of a consumer group offset.
	 *
	 * @param clusterId Cluster id to filter
	 * @param groupId   Group id to filter
	 * @return Consumer group offset details.
	 */
	public ConsumerGroupOffsetDto get(String clusterId, String groupId) {

		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		try {
			// Get all data from kafka
			TopicPartition partition = new TopicPartition("plop", 0);
			ListConsumerGroupOffsetsOptions test = new ListConsumerGroupOffsetsOptions();
			test.topicPartitions(Collections.singletonList(partition));
			Map<TopicPartition, OffsetAndMetadata> raw = zooKaMixAdmin.getAdminClient()
					.listConsumerGroupOffsets(groupId, test).partitionsToOffsetAndMetadata().get();
			ConsumerGroupOffsetDto response = new ConsumerGroupOffsetDto(groupId);
			// Convert the data in dto format.
			raw.entrySet().forEach(it -> {
				ConsumerGroupTopicPartitionDto key = topicMapper.mapFromKafkaNative(it.getKey());
				OffsetAndMetadataDto value = offsetAndMetadataMapper.mapToDto(it.getValue());
				response.add(key, value);
			});
			return response;
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka to retrieve consumer group detail", e);
		}
	}

	/**
	 * Delete the group consumer offset details.
	 *
	 * @param clusterId Cluster to filter.
	 * @param groupId   Group id to filter
	 * @param topics    List of topics to apply the delete
	 */
	public void delete(String clusterId, String groupId, List<ConsumerGroupTopicPartitionDto> topics) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		Set<TopicPartition> toDelete = topics.stream().map(topicMapper::mapToKafkaNative).collect(Collectors.toSet());

		// Todo: handle futures
		zooKaMixAdmin.getAdminClient().deleteConsumerGroupOffsets(groupId, toDelete);
	}

	/**
	 * Update group consumer offsets.
	 *
	 * @param clusterId Cluster to filter
	 * @param groupId   Group to filter
	 * @param mapTopics List of topic with the new group consumer offset.
	 */
	public void alterConsumerGroup(String clusterId, String groupId,
			Map<ConsumerGroupTopicPartitionDto, OffsetAndMetadataDto> mapTopics) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		Map<TopicPartition, OffsetAndMetadata> mapToUpdate = new HashMap<>();

		mapTopics.entrySet().forEach(it -> {
			TopicPartition key = topicMapper.mapToKafkaNative(it.getKey());
			OffsetAndMetadata value = offsetAndMetadataMapper.mapToNative(it.getValue());
			mapToUpdate.put(key, value);
		});
		zooKaMixAdmin.getAdminClient().alterConsumerGroupOffsets(groupId, mapToUpdate);
	}

	/**
	 * Reset consumer offset.
	 *
	 * @param clusterId the cluster ID
	 * @param groupId   the group ID
	 * @param request   the request
	 */
	public void resetOffset(String clusterId, String groupId, ConsumerGroupOffsetResetDto request) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Map offsets
		Map<TopicPartition, OffsetAndMetadata> offsets = request.getOffsets().stream().collect(Collectors.toMap(e -> {
			return new TopicPartition(e.getTopic(), e.getPartition());
		}, e -> {
			return new OffsetAndMetadata(e.getOffset());
		}));

		// Execute kafka request
		try {
			zooKaMixAdmin.getAdminClient().alterConsumerGroupOffsets(groupId, offsets).all().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Set group offset from another group offset.
	 *
	 * @param clusterId     the cluster id
	 * @param sourceGroupId the group to update
	 * @param targetGroupId the targeted group
	 * @param offset 		the offset
	 */
	public void setGroupOffsetFromAnotherGroupOffset(String clusterId, String sourceGroupId, String targetGroupId, long offset) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Map offsets
		try {
			// Get origin consumer group
			Map<String, ConsumerGroupDescription> targetGroup = zooKaMixAdmin.getAdminClient()
					.describeConsumerGroups(Arrays.asList(targetGroupId)).all().get();
			KafkaOffsets offsets = this.getOffsets(zooKaMixAdmin, targetGroup);

			// Reset offset
			ConsumerGroupOffsetResetDto request = new ConsumerGroupOffsetResetDto();
			request.setOffsets(offsets.getOffsets().stream().filter(e -> e.getOffset() > 0).map(e -> {
				return new ConsumerGroupOffsetPartitionResetDto(e.getTopic(), e.getPartition(), e.getOffset() + offset);
			}).collect(Collectors.toList()));
			if (CollectionUtils.isNotEmpty(request.getOffsets())) {
				log.debug("Set following offset {} on group {}", request.getOffsets(), sourceGroupId);
				this.resetOffset(clusterId, sourceGroupId, request);
			} else {
				log.warn("Cannot find offsets for group {}", targetGroupId);
			}

		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}

	}

}
