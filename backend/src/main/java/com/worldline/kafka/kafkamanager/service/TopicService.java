package com.worldline.kafka.kafkamanager.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.setting.ConfigurationUpdateDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicAddPartitionDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicCreationDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicReassignDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicSearchDto;
import com.worldline.kafka.kafkamanager.exception.KafkaException;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.TopicMapper;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

/**
 * Topic service.
 */
@Slf4j
@Service
public class TopicService {

	private final ZooKaMixService zooKaMixService;

	private final PageableService pageableService;

	private TopicMapper topicMapper;

	private final ConsumerGroupOffsetService consumerGroupOffsetService;

	/**
	 * Constructor.
	 *
	 * @param zooKaMixService            the zookeeper kafka jmx service
	 * @param pageableService            the pageable service
	 * @param topicMapper                the topic mapper
	 * @param consumerGroupOffsetService the consumer offset service
	 */
	@Autowired
	public TopicService(ZooKaMixService zooKaMixService, PageableService pageableService, TopicMapper topicMapper,
			ConsumerGroupOffsetService consumerGroupOffsetService) {
		this.zooKaMixService = zooKaMixService;
		this.pageableService = pageableService;
		this.topicMapper = topicMapper;
		this.consumerGroupOffsetService = consumerGroupOffsetService;
	}

	/**
	 * Get topic list.
	 *
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @param pageable  the pageable data
	 * @return the topic list
	 */
	public Page<TopicResponseDto> list(String clusterId, TopicSearchDto request, Pageable pageable) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Get topic data
		try {
			// Get topic name
			Set<String> topicNames = zooKaMixAdmin.getTopicNames();
			// Get topic description
			Map<String, TopicDescription> topics = zooKaMixAdmin.getAdminClient().describeTopics(topicNames).all()
					.get();

			// Get topic data
			List<TopicResponseDto> result = getTopics(zooKaMixAdmin, topics, request.isFull());

			// Map to page
			return pageableService.createPage(result, pageable);
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Get topic data.
	 * 
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 * @return the topic data
	 */
	public TopicResponseDto get(String clusterId, String topicName) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Get topic data
		try {
			Map<String, TopicDescription> topic = zooKaMixAdmin.getAdminClient()
					.describeTopics(Arrays.asList(topicName)).all().get();
			List<TopicResponseDto> topics = getTopics(zooKaMixAdmin, topic, true);
			if (CollectionUtils.isNotEmpty(topics)) {
				return topics.get(0);
			}
		} catch (InterruptedException | ExecutionException e) {
			if (e.getCause() instanceof UnknownTopicOrPartitionException) {
				log.error("Cannot find topic " + topicName + " in cluster " + clusterId);
			} else {
				throw new KafkaException("Cannot connect to kafka", e);
			}
		}
		return null;
	}

	/**
	 * Get topics data
	 * 
	 * @param zooKaMixAdmin the connection
	 * @param topics        the topic list
	 * @param isFull        {@code true} for full data
	 * @return the topics data
	 * @throws InterruptedException
	 */
	private List<TopicResponseDto> getTopics(ZooKaMixAdmin zooKaMixAdmin, Map<String, TopicDescription> topics,
			boolean isFull) throws InterruptedException {
		// Get broker list
		final List<Integer> brokerList = isFull ? zooKaMixAdmin.getBrokerList() : null;

		return topics.entrySet().stream().map(e -> {
			// Get topic data
			String name = e.getKey();
			final TopicDescription topic = topics.get(name);

			// Get end offset
			final Map<Integer, Long> endOffsets = isFull
					? consumerGroupOffsetService.getEndOffset(zooKaMixAdmin, name,
							topic.partitions().stream().map(TopicPartitionInfo::partition).collect(Collectors.toList()))
					: null;

			// Get topic configuration
			Map<String, String> topicConfig = null;
			if (isFull) {
				try {
					topicConfig = zooKaMixAdmin.getTopicConfig(name);
				} catch (InterruptedException e1) {
					throw new KafkaException("Cannot connect to kafka", e1);
				}
			}

			// Map result
			return topicMapper.mapping(topic, brokerList, endOffsets, topicConfig, isFull);
		}).collect(Collectors.toList());
	}

	/**
	 * Create topic.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the create request
	 */
	public void createTopic(String clusterId, TopicCreationDto request) {
		// Create topic
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		NewTopic newTopic = new NewTopic(request.getName(), request.getNbPartitions(), request.getReplicationFactor());
		CreateTopicsOptions createTopicsOptions = new CreateTopicsOptions();
		createTopicsOptions.timeoutMs(5000);

		try {
			zooKaMixAdmin.getAdminClient().createTopics(Collections.singleton(newTopic), createTopicsOptions).all()
					.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}

		// Set configurations
		if (request.getConfiguration() != null && request.getConfiguration().size() > 0) {
			try {
				zooKaMixAdmin.setTopicConfig(request.getName(), request.getConfigurationMap());
			} catch (InterruptedException e) {
				throw new KafkaException("Cannot connect to kafka", e);
			}
		}
	}

	/**
	 * Delete topic.
	 * 
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 */
	public void deleteTopic(String clusterId, String topicName) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		try {
			DeleteTopicsOptions options = new DeleteTopicsOptions();
			options.timeoutMs(5000);
			zooKaMixAdmin.getAdminClient().deleteTopics(Collections.singleton(topicName), options).all().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Reassign partition of a topic.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the reassign request
	 * @param topicId   the topic ID
	 */
	public void reassign(String clusterId, TopicReassignDto request, String topicId) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		try {
			zooKaMixAdmin.reassignPartition(topicId, request.getPartitionsMap());
		} catch (InterruptedException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Update topic configuration.
	 * 
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 * @param request   the update request
	 */
	public void updateConfiguration(String clusterId, String topicName, ConfigurationUpdateDto request) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		if (request.getConfiguration() != null && request.getConfiguration().size() > 0) {
			try {
				zooKaMixAdmin.setTopicConfig(topicName, request.getConfigurationMap());
			} catch (InterruptedException e) {
				throw new KafkaException("Cannot connect to kafka", e);
			}
		}
	}

	/**
	 * Add partition to an existing topic.
	 * 
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 * @param request   the create request
	 */
	public void addPartition(String clusterId, String topicName, TopicAddPartitionDto request) {
		// Get connection
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		try {
			zooKaMixAdmin.addPartition(topicName, request.getNbPartitions(), request.getNewAssignments());
		} catch (InterruptedException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

}
