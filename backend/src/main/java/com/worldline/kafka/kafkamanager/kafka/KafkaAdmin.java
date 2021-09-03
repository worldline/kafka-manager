package com.worldline.kafka.kafkamanager.kafka;

import java.io.Closeable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.CreatePartitionsOptions;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewPartitionReassignment;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.ConfigResource.Type;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;

import com.worldline.kafka.kafkamanager.exception.KafkaException;

import kafka.common.OffsetAndMetadata;
import kafka.coordinator.group.OffsetKey;
import org.springframework.util.StringUtils;

/**
 * Kafka admin.
 */
public class KafkaAdmin implements Closeable {

	private final Properties kafkaProps;
	private AdminClient adminClient;
	private final KafkaOffsets offsets = new KafkaOffsets();

	/**
	 * Constructor.
	 *
	 * @param kafkaProps the kafka properties
	 */
	public KafkaAdmin(Properties kafkaProps) {
		this.kafkaProps = kafkaProps;
	}

	/**
	 * Get kafka admin client.
	 *
	 * @return the kafka admin client
	 */
	public AdminClient getAdminClient() {
		if (adminClient == null) {
			this.adminClient = AdminClient.create(kafkaProps);
		}
		return adminClient;
	}

	/**
	 * Add consumer offset.
	 *
	 * @param offsetKey         the offset key
	 * @param offsetAndMetadata the offset metadata
	 */
	public void addConsumerOffset(OffsetKey offsetKey, OffsetAndMetadata offsetAndMetadata) {
		String topic = offsetKey.key().topicPartition().topic();
		KafkaOffset offset = offsets.get(topic, offsetKey.key().topicPartition().partition(), offsetKey.key().group())
				.orElseGet(() -> {
					KafkaOffset newOffset = new KafkaOffset();
					newOffset.setTopic(topic);
					newOffset.setPartition(offsetKey.key().topicPartition().partition());
					newOffset.setGroup(offsetKey.key().group());
					offsets.getOffsets().add(newOffset);
					return newOffset;
				});
		offset.setOffset(offsetAndMetadata.offset());
	}

	/**
	 * Get current offset
	 *
	 * @param consumerGroups consumer groups
	 * @return the kafka offsets
	 */
	public KafkaOffsets getCurrentOffset(Map<String, ConsumerGroupDescription> consumerGroups) {
		KafkaOffsets result = new KafkaOffsets();
		try {
			for (String groupId : consumerGroups.keySet()) {
				ListConsumerGroupOffsetsOptions options = new ListConsumerGroupOffsetsOptions();
				options.timeoutMs(5000);
				ListConsumerGroupOffsetsResult listConsumerGroupOffsets = getAdminClient()
						.listConsumerGroupOffsets(groupId, options);
				Map<TopicPartition, org.apache.kafka.clients.consumer.OffsetAndMetadata> offsets = listConsumerGroupOffsets
						.partitionsToOffsetAndMetadata().get();
				offsets.forEach((key, value) -> result.getOffsets()
						.add(new KafkaOffset(key.topic(), key.partition(), groupId, value.offset())));
			}
		} catch (InterruptedException | ExecutionException e1) {
			consumerGroups.forEach((key, value) -> value.members().forEach(m -> {
				m.assignment().topicPartitions().forEach(p -> {
					Optional<KafkaOffset> offset = offsets.get(p.topic(), p.partition(), key);
					if (offset.isPresent()) {
						result.getOffsets().add(offset.get());
					} else {
						result.getOffsets().add(new KafkaOffset(p.topic(), p.partition(), value.groupId(), -1));
					}
				});
			}));
		}
		return result;
	}

	/**
	 * Get configuration on a kafka resource.
	 *
	 * @param type       the resource type
	 * @param resourceId the resource name
	 * @return the configuration
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Map<String, String> getConfig(Type type, String resourceId) throws InterruptedException, ExecutionException {
		ConfigResource resource = new ConfigResource(type, resourceId);
		DescribeConfigsResult describeConfigsResult = getAdminClient().describeConfigs(Collections.singleton(resource));
		Map<ConfigResource, Config> topicConfig = describeConfigsResult.all().get();
		Config config = topicConfig.get(resource);
		return config.entries().stream()
			.filter(entry -> Objects.nonNull(entry) && StringUtils.hasText(entry.name()))
			.map(e -> Objects.isNull(e.value()) ? new ConfigEntry(e.name(), "") : e)
			.collect(Collectors.toMap(ConfigEntry::name, ConfigEntry::value));
	}

	/**
	 * Set configuration on a resource.
	 *
	 * @param type          the resource type
	 * @param resourceId    the resource ID
	 * @param configuration the configuration
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void setConfig(Type type, String resourceId, Map<String, String> configuration)
			throws InterruptedException, ExecutionException {
		Map<ConfigResource, Collection<AlterConfigOp>> configs = new HashMap<>();
		List<AlterConfigOp> topicConfig = configuration.entrySet().stream().map(e -> {
			return new AlterConfigOp(new ConfigEntry(e.getKey(), e.getValue()), AlterConfigOp.OpType.SET);
		}).collect(Collectors.toList());
		configs.put(new ConfigResource(type, resourceId), topicConfig);
		AlterConfigsOptions options = new AlterConfigsOptions();
		options.timeoutMs(5000);
		getAdminClient().incrementalAlterConfigs(configs, options).all().get();
	}

	/**
	 * Add partition to a topic.
	 *
	 * @param topicName      the topic name
	 * @param nbPartitions   the nb partition
	 * @param newAssignments the new assignement list
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void addPartition(String topicName, int nbPartitions, List<List<Integer>> newAssignments)
			throws InterruptedException, ExecutionException {
		// Create partition
		Map<String, NewPartitions> newPartitions = new HashMap<>();
		if (newAssignments != null) {
			newPartitions.put(topicName, NewPartitions.increaseTo(nbPartitions, newAssignments));
		} else {
			newPartitions.put(topicName, NewPartitions.increaseTo(nbPartitions));
		}
		// Set options
		CreatePartitionsOptions options = new CreatePartitionsOptions();
		options.timeoutMs(5000);
		getAdminClient().createPartitions(newPartitions, options).all().get();
	}

	/**
	 * Reassign partition.
	 *
	 * @param topicName the topic name
	 * @param data      the partition assignment data
	 */
	public void reassignPartition(String topicName, Map<Integer, List<Integer>> data)
			throws InterruptedException, ExecutionException {
		Map<TopicPartition, Optional<NewPartitionReassignment>> reassignments = new HashMap<>();
		data.forEach((partition, value) -> {
			TopicPartition topicPartition = new TopicPartition(topicName, partition);
			NewPartitionReassignment newPartitionReassignment = new NewPartitionReassignment(value);
			reassignments.put(topicPartition, Optional.of(newPartitionReassignment));
		});
		getAdminClient().alterPartitionReassignments(reassignments).all().get();
	}

	/**
	 * Get topic names.
	 *
	 * @return the topic names
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Set<String> getTopicNames() throws InterruptedException, ExecutionException {
		ListTopicsResult listTopic = getAdminClient().listTopics();
		return listTopic.names().get();
	}

	/**
	 * Create consumer for topic.
	 *
	 * @param <T>  the consumer type
	 * @param type the consumer type
	 * @return the kafka consumer
	 */
	public <T> KafkaConsumer<T, T> createConsumer(Class<T> type) {
		return createConsumer(type, "KM-" + UUID.randomUUID().toString(), false, 1, false, true);
	}

	/**
	 * Create consumer.
	 *
	 * @param <T>          the type
	 * @param type         the type
	 * @param groupId      the group id
	 * @param begin        {@code true} to start at the beginning of the topic
	 * @param maxPoll      the max poll
	 * @param autoCommit   the auto commit
	 * @param excludeTopic the exclude topic
	 * @return the kafka consumer
	 */
	public <T> KafkaConsumer<T, T> createConsumer(Class<T> type, String groupId, Boolean begin, int maxPoll,
			boolean autoCommit, boolean excludeTopic) {
		Properties propConsumer = buildConsumerProperties(groupId, begin, maxPoll, autoCommit, excludeTopic);
		if (String.class.equals(type)) {
			propConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			propConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		} else if (Byte[].class.equals(type) || byte[].class.equals(type)) {
			propConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
			propConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		} else if (Bytes.class.equals(type)) {
			propConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
			propConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
		} else {
			throw new KafkaException("Cannot find deserializer");
		}
		return new KafkaConsumer<>(propConsumer);
	}

	/**
	 * Get consumer properties.
	 *
	 * @param groupId      the group id
	 * @param begin        {@code true} to start at the beggining
	 * @param maxPoll      the max poll
	 * @param autoCommit   the auto commit
	 * @param excludeTopic the exclude topic
	 * @return the kafka consumer properties
	 */
	private Properties buildConsumerProperties(String groupId, Boolean begin, int maxPoll, boolean autoCommit,
			boolean excludeTopic) {
		// Create properties
		Properties propConsumer = new Properties();
		propConsumer.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
				kafkaProps.get(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));
		propConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		propConsumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
		propConsumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(maxPoll));
		propConsumer.put(ConsumerConfig.CLIENT_ID_CONFIG, groupId);
		propConsumer.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG, String.valueOf(excludeTopic));
		if (begin != null) {
			propConsumer.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, begin ? "earliest" : "latest");
		}
		return propConsumer;
	}

	/**
	 * Create kafka producer.
	 *
	 * @return kafka producer
	 */
	public <T> KafkaProducer<T, T> createKafkaProducer(Class<T> type) {
		Properties props = new Properties();
		props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
				kafkaProps.get(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "ProducerAdminService");
		if (String.class.equals(type)) {
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		} else if (Byte[].class.equals(type) || byte[].class.equals(type)) {
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		} else {
			throw new KafkaException("Cannot find deserializer");
		}
		return new KafkaProducer<>(props);
	}

	@Override
	public void close() {
		if (adminClient != null) {
			adminClient.close();
		}
	}

}
