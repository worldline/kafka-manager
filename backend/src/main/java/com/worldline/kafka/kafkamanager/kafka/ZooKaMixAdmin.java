package com.worldline.kafka.kafkamanager.kafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.management.remote.JMXConnector;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.config.ConfigResource.Type;
import org.apache.kafka.common.errors.UnsupportedVersionException;
import org.apache.zookeeper.ZooKeeper;

import com.worldline.kafka.kafkamanager.client.KafkaConnectClient;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsResponseDto;

import kafka.common.OffsetAndMetadata;
import kafka.coordinator.group.OffsetKey;
import lombok.Getter;

/**
 * Connection admin.
 */
public class ZooKaMixAdmin implements Closeable {

	@Getter
	private final String clusterId;

	private final KafkaAdmin kafkaAdmin;

	private final JmxAdmin jmxAdmin;

	private final ZookeeperAdmin zookeeperAdmin;

	private final KafkaConnectClient kafkaConnectClient;

	private boolean isClosed = false;

	/**
	 * Constructor.
	 * 
	 * @param clusterId          the cluster ID
	 * @param kafkaProps         the kafka properties
	 * @param zkUrl              the zookeeper url
	 * @param jmxAdmin           the jmx admin data
	 * @param kafkaConnectClient the kafka connect client
	 */
	public ZooKaMixAdmin(String clusterId, Properties kafkaProps, String zkUrl, JmxAdmin jmxAdmin,
			KafkaConnectClient kafkaConnectClient) {
		this.clusterId = clusterId;
		this.jmxAdmin = jmxAdmin;
		this.kafkaAdmin = new KafkaAdmin(kafkaProps);
		this.zookeeperAdmin = new ZookeeperAdmin(zkUrl);
		this.kafkaConnectClient = kafkaConnectClient;

	}

	/**
	 * Get kafka admin client.
	 * 
	 * @return the kafka admin client
	 */
	public AdminClient getAdminClient() {
		return kafkaAdmin.getAdminClient();
	}

	/**
	 * Get zookeeper client.
	 * 
	 * @return the zookeeper client
	 */
	public ZooKeeper getZookeeperClient() {
		return zookeeperAdmin.getZookeeperClient();
	}

	/**
	 * Get kafka connect client.
	 * 
	 * @return kafka connect client
	 */
	public KafkaConnectClient getKafkaConnectClient() {
		return kafkaConnectClient;
	}

	/**
	 * Get JMX connector.
	 * 
	 * @return the JMX connector
	 */
	public JMXConnector getJmxConnector() {
		return jmxAdmin.getJmxConnector();
	}

	public boolean isOpen() {
		return !isClosed;
	}

	@Override
	public void close() throws IOException {
		this.isClosed = true;
		if (zookeeperAdmin != null) {
			zookeeperAdmin.close();
		}
		if (jmxAdmin != null) {
			jmxAdmin.close();
		}
		if (kafkaAdmin != null) {
			kafkaAdmin.close();
		}
	}

	/**
	 * Get configuration on a resource.
	 * 
	 * @param type      the resource type
	 * @param topicName the resource ID
	 * @return the configuration
	 * @throws InterruptedException
	 */
	public Map<String, String> getConfig(Type type, String topicName) throws InterruptedException {
		try {
			return kafkaAdmin.getConfig(type, topicName);
		} catch (UnsupportedVersionException | ExecutionException e) {
			return zookeeperAdmin.getConfig(type, topicName);
		}
	}

	/**
	 * Set configuration on a resource.
	 * 
	 * @param type          the resource type
	 * @param topicName     the resource ID
	 * @param configuration the configuration
	 * @throws InterruptedException
	 */
	public void setConfig(Type type, String topicName, Map<String, String> configuration) throws InterruptedException {
		try {
			kafkaAdmin.setConfig(type, topicName, configuration);
		} catch (UnsupportedVersionException | ExecutionException e) {
			zookeeperAdmin.setConfig(type, topicName, configuration);
		}
	}

	/**
	 * Get configuration on a topic.
	 * 
	 * @param topicName the topic name
	 * @return the configuration
	 * @throws InterruptedException
	 */
	public Map<String, String> getTopicConfig(String topicName) throws InterruptedException {
		return getConfig(Type.TOPIC, topicName);
	}

	/**
	 * Get configuration on a broker.
	 * 
	 * @param brokerId the broker ID
	 * @return the configuration
	 * @throws InterruptedException
	 */
	public Map<String, String> getBrokerConfig(String brokerId) throws InterruptedException {
		return getConfig(Type.BROKER, brokerId);
	}

	/**
	 * Set configuration on a topic.
	 * 
	 * @param topicName     the topic name
	 * @param configuration the configuration
	 * @throws InterruptedException
	 */
	public void setTopicConfig(String topicName, Map<String, String> configuration) throws InterruptedException {
		setConfig(Type.TOPIC, topicName, configuration);
	}

	/**
	 * Set configuration on a broker.
	 * 
	 * @param brokerId      the broker ID
	 * @param configuration the configuration
	 * @throws InterruptedException
	 */
	public void setBrokerConfig(String brokerId, Map<String, String> configuration) throws InterruptedException {
		setConfig(Type.BROKER, brokerId, configuration);
	}

	/**
	 * Add partition to a topic.
	 * 
	 * @param topicName      the topic name
	 * @param nbPartitions   the nb partition
	 * @param newAssignments the new assignement list
	 * @throws InterruptedException
	 */
	public void addPartition(String topicName, int nbPartitions, List<List<Integer>> newAssignments)
			throws InterruptedException {
		try {
			kafkaAdmin.addPartition(topicName, nbPartitions, newAssignments);
		} catch (UnsupportedVersionException | ExecutionException e) {
			zookeeperAdmin.addPartition(topicName, nbPartitions, newAssignments);
		}
	}

	/**
	 * Reassign partition.
	 * 
	 * @param topicName the topic name
	 * @param data      the partition assignment data
	 */
	public void reassignPartition(String topicName, Map<Integer, List<Integer>> data) throws InterruptedException {
		try {
			kafkaAdmin.reassignPartition(topicName, data);
		} catch (UnsupportedVersionException | ExecutionException e) {
			zookeeperAdmin.reassignPartition(topicName, data);
		}
	}

	/**
	 * Get topic names.
	 * 
	 * @return the topic names
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Set<String> getTopicNames() throws InterruptedException {
		try {
			return kafkaAdmin.getTopicNames();
		} catch (UnsupportedVersionException | ExecutionException e) {
			return zookeeperAdmin.getTopicNames();
		}
	}

	/**
	 * Get broker list.
	 */
	public List<Integer> getBrokerList() throws InterruptedException {
		return zookeeperAdmin.getBrokerList();
	}

	/**
	 * Get kafka version.
	 * 
	 * @param brokerId the broker ID
	 * @return the kafka version
	 */
	public Optional<String> getKafkaVersion(String brokerId) {
		return jmxAdmin.getKafkaVersion(brokerId);
	}

	/**
	 * Get metrics topic.
	 * 
	 * @param topicName the topic name
	 * @return the metrics related to the topic
	 */
	public TopicMetricsResponseDto getMetricsTopic(String topicName) {
		return jmxAdmin.getMetricsTopic(topicName);
	}

	/**
	 * Create kafka consumer to read byte array data.
	 * 
	 * @return the kafka consumer
	 */
	public <T> KafkaConsumer<T, T> createConsumer(Class<T> type) {
		return kafkaAdmin.createConsumer(type);
	}

	/**
	 * Create kafka consumer.
	 * 
	 * @param <T>          the type
	 * @param type         the type
	 * @param groupId      the group id
	 * @param begin        {@code true} to start at the beginning of the topic
	 * @param maxPool      the max pool
	 * @param autoCommit   the auto commit
	 * @param excludeTopic the exclude topic
	 * @return the kafka consumer
	 */
	public <T> KafkaConsumer<T, T> createConsumer(Class<T> type, String groupId, Boolean begin, int maxPool,
			boolean autoCommit, boolean excludeTopic) {
		return kafkaAdmin.createConsumer(type, groupId, begin, maxPool, autoCommit, excludeTopic);
	}

	/**
	 * Add consumer offset.
	 * 
	 * @param offsetKey         the offset key
	 * @param offsetAndMetadata the offset metadata
	 */
	public void addConsumerOffset(OffsetKey offsetKey, OffsetAndMetadata offsetAndMetadata) {
		kafkaAdmin.addConsumerOffset(offsetKey, offsetAndMetadata);
	}

	/**
	 * Get current offset.
	 * 
	 * @param consumerGroups consumer groups
	 * @return the kafka offsets
	 */
	public KafkaOffsets getCurrentOffset(Map<String, ConsumerGroupDescription> consumerGroups) {
		return kafkaAdmin.getCurrentOffset(consumerGroups);
	}

	/**
	 * Create kafka producer.
	 * 
	 * @return Kafka producer.
	 */
	public KafkaProducer<byte[], byte[]> createProducer() {
		return kafkaAdmin.createKafkaProducer(byte[].class);
	}

}
