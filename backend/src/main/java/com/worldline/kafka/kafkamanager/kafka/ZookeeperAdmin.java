package com.worldline.kafka.kafkamanager.kafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.common.config.ConfigResource.Type;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldline.kafka.kafkamanager.exception.ConnectionException;
import com.worldline.kafka.kafkamanager.exception.ZookeeperException;
import com.worldline.kafka.kafkamanager.zk.ZKConnection;
import com.worldline.kafka.kafkamanager.zk.ZKTopicConfig;
import com.worldline.kafka.kafkamanager.zk.ZKTopicPartition;
import com.worldline.kafka.kafkamanager.zk.ZkTopicPartitionAssignment;
import com.worldline.kafka.kafkamanager.zk.ZkTopicPartitionReassign;

import lombok.extern.slf4j.Slf4j;

/**
 * Zookeeper admin.
 */
@Slf4j
public class ZookeeperAdmin implements Closeable {

	private static final String BROKER_TOPICS_PATH = "/brokers/topics";
	private static final String BROKER_TOPIC_CONFIG_PATH = "/config/topics";
	private static final String BROKER_CONFIG_PATH = "/config/brokers";
	private static final String BROKER_LIST = "/brokers/ids";
	private static final String REASSIGN_PARTITION_PATH = "/admin/reassign_partitions";

	private ZooKeeper zooKeeper;

	private final String zkUrl;

	private final ObjectMapper zkMapper = new ObjectMapper();

	/**
	 * Constructor.
	 * 
	 * @param zkUrl the zookeeper url
	 */
	public ZookeeperAdmin(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	/**
	 * Get zookeeper client.
	 * 
	 * @return the zookeeper client
	 */
	public ZooKeeper getZookeeperClient() {
		if (zooKeeper == null) {
			// ZK connection
			ZKConnection zkConnection = new ZKConnection();
			try {
				zooKeeper = zkConnection.connect(zkUrl);
			} catch (IOException | InterruptedException e) {
				throw new ZookeeperException("Cannot connect to zookeeper", e);
			}
		}
		return zooKeeper;
	}

	/**
	 * Read data from zookeeper.
	 * 
	 * @param <T>  type class mapping
	 * @param path the zookeeper path
	 * @param type the class mapping
	 * @return the data
	 */
	private <T> T readDataFromZk(String path, Class<T> type) {
		String data;
		try {
			data = new String(getZookeeperClient().getData(path, null, null));
			return zkMapper.readValue(data, type);
		} catch (KeeperException | InterruptedException | JsonProcessingException e) {
			log.error("Cannot read data from zookeeper", e);
			throw new ZookeeperException("Cannot read data", e);
		}
	}

	/**
	 * Get configuration on a resource.
	 * 
	 * @param type       the resource type
	 * @param resourceId the resource ID
	 * @return the configuration
	 * @throws InterruptedException
	 */
	public Map<String, String> getConfig(Type type, String resourceId) throws InterruptedException {
		String zookeeperPath = Type.BROKER.equals(type) ? BROKER_CONFIG_PATH : BROKER_TOPIC_CONFIG_PATH;
		ZKTopicConfig topicConfig = readDataFromZk(zookeeperPath + "/" + resourceId, ZKTopicConfig.class);
		return topicConfig.getConfig();
	}

	/**
	 * Set configuration on a topic.
	 * 
	 * @param type          the resource type
	 * @param resourceId    the resource ID
	 * @param configuration the configuration
	 * @throws InterruptedException
	 */
	public void setConfig(Type type, String resourceId, Map<String, String> configuration) throws InterruptedException {
		String zookeeperPath = Type.BROKER.equals(type) ? BROKER_CONFIG_PATH : BROKER_TOPIC_CONFIG_PATH;
		ZKTopicConfig topicConfig = readDataFromZk(zookeeperPath + "/" + resourceId, ZKTopicConfig.class);
		configuration.entrySet().stream().forEach(entry -> {
			topicConfig.getConfig().put(entry.getKey(), entry.getValue());
		});
		try {
			zooKeeper.setData(zookeeperPath + "/" + resourceId, zkMapper.writeValueAsString(topicConfig).getBytes(),
					-1);
		} catch (JsonProcessingException | KeeperException e1) {
			throw new ZookeeperException("Cannot write data in zk", e1);
		}
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
		ZKTopicPartition zkTopic = readDataFromZk(BROKER_TOPICS_PATH + "/" + topicName, ZKTopicPartition.class);
		List<Integer> existingPartition = zkTopic.getPartitions().values().iterator().next();
		for (int i = zkTopic.getPartitions().size(), j = 0; i < nbPartitions; i++, j++) {
			if (newAssignments != null) {
				zkTopic.getPartitions().put(String.valueOf(i), newAssignments.get(j));
			} else {
				zkTopic.getPartitions().put(String.valueOf(i), existingPartition);
			}
		}
		try {
			zooKeeper.setData(BROKER_TOPICS_PATH + "/" + topicName, zkMapper.writeValueAsString(zkTopic).getBytes(),
					-1);
		} catch (JsonProcessingException | KeeperException | InterruptedException e1) {
			throw new ZookeeperException("Cannot write data in zk", e1);
		}
	}

	/**
	 * Reassign partition.
	 * 
	 * @param topicName the topic name
	 * @param data      the partition assignment data
	 */
	public void reassignPartition(String topicName, Map<Integer, List<Integer>> data) throws InterruptedException {
		// Create request
		List<ZkTopicPartitionAssignment> assignments = data.entrySet().stream().map(e -> {
			ZkTopicPartitionAssignment assignment = new ZkTopicPartitionAssignment();
			assignment.setTopic(topicName);
			assignment.setPartition(e.getKey());
			assignment.setReplicas(e.getValue());
			return assignment;
		}).collect(Collectors.toList());
		ZkTopicPartitionReassign request = new ZkTopicPartitionReassign();
		request.setVersion("1");
		request.setPartitions(assignments);

		// Execution
		try {
			zooKeeper.create(REASSIGN_PARTITION_PATH, zkMapper.writeValueAsString(request).getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (JsonProcessingException | KeeperException | InterruptedException e1) {
			throw new ZookeeperException("Cannot write data in zk", e1);
		}
	}

	/**
	 * Get topic names.
	 * 
	 * @return the topic names
	 * @throws InterruptedException
	 */
	public Set<String> getTopicNames() throws InterruptedException {
		try {
			List<String> topics = getZookeeperClient().getChildren(BROKER_TOPICS_PATH, null);
			return new HashSet<String>(topics);
		} catch (KeeperException | InterruptedException e1) {
			log.error("Cannot get topic names", e1);
			throw new ZookeeperException("Cannot connect", e1);
		}
	}

	/**
	 * Get broker list.
	 */
	public List<Integer> getBrokerList() throws InterruptedException {
		try {
			List<String> children = getZookeeperClient().getChildren(BROKER_LIST, null);
			return children.stream().map(Integer::valueOf).collect(Collectors.toList());
		} catch (KeeperException e) {
			throw new ConnectionException("Cannot connect", e);
		}
	}

	@Override
	public void close() throws IOException {
		if (zooKeeper != null) {
			try {
				zooKeeper.close();
			} catch (InterruptedException e) {
				throw new ZookeeperException("Cannot close zookeeper connection", e);
			}
		}
	}

}
