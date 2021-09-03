package com.worldline.kafka.kafkamanager.service.zookamix;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.client.KafkaConnectClient;
import com.worldline.kafka.kafkamanager.exception.KafkaException;
import com.worldline.kafka.kafkamanager.kafka.JmxAdmin;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.model.Cluster;
import com.worldline.kafka.kafkamanager.properties.JmxProperties;
import com.worldline.kafka.kafkamanager.service.BatchConsumerOffsetService;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Zookeeper, kafka, JMX abstract service.
 */
@Slf4j
public abstract class ZooKaMixService {

	protected Map<String, ZooKaMixAdmin> connections = new HashMap<>();
	protected JmxProperties jmxProperties;
	protected BatchConsumerOffsetService batchConsumerOffsetService;
	protected Decoder decoder;
	protected Encoder encoder;

	/**
	 * Constructor.
	 *
	 * @param batchConsumerOffsetService the batch consumer offset service
	 * @param jmxProperties              the jmx properties
	 * @param encoder                    the spring encoder
	 * @param decoder                    the spring decoder
	 */
	public ZooKaMixService(BatchConsumerOffsetService batchConsumerOffsetService, JmxProperties jmxProperties,
			Decoder decoder, Encoder encoder) {
		this.batchConsumerOffsetService = batchConsumerOffsetService;
		this.jmxProperties = jmxProperties;
		this.encoder = encoder;
		this.decoder = decoder;
	}

	/**
	 * Get connection.
	 * 
	 * @param clusterId the cluster ID
	 * @return the connection
	 */
	public abstract ZooKaMixAdmin getConnection(String clusterId);

	/**
	 * Drop connection.
	 *
	 * @param clusterId the cluster ID
	 */
	public void dropConnection(String clusterId) {
		ZooKaMixAdmin connection = getConnection(clusterId);
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				log.warn("Cannot close connection", e);
			}
			connections.remove(clusterId);
		}
	}

	/**
	 * Get connections.
	 * 
	 * @return the connection list
	 */
	public abstract List<ZooKaMixAdmin> getConnections();

	/**
	 * Create connection.
	 *
	 * @param cluster the cluster entity
	 * @return the connection
	 */
	protected ZooKaMixAdmin createConnection(Cluster cluster) {
		// Get jmx url
		JmxAdmin jmxAdmin = null;
		List<String> jmxAddress = cluster.getJmxAddress();
		if (!CollectionUtils.isEmpty(jmxAddress)) {
			jmxAdmin = new JmxAdmin(jmxProperties, jmxAddress);
		}

		// Get kafka connect url
		String kafkaConnectAddr = cluster.getKafkaConnectAddr();
		KafkaConnectClient kafkaConnectClient = null;
		if (StringUtils.hasText(kafkaConnectAddr)) {
			kafkaConnectClient = createKafkaConnectClient(kafkaConnectAddr);
		}

		ZooKaMixAdmin result = new ZooKaMixAdmin(cluster.getId(), getConnection(cluster.getKafkaAddress()),
				cluster.getZkAddr(), jmxAdmin, kafkaConnectClient);

		// Start offsets batch for old version
		if (StringUtils.hasText(cluster.getKafkaVersion()) && cluster.getKafkaVersion().startsWith("0.10.1")) {
			batchConsumerOffsetService.consumeOffset(result);
		}
		return result;
	}

	/**
	 * Create kafka connect client based on url.
	 * 
	 * @param address the kafka connect url
	 * @return the kafka connect client
	 */
	protected KafkaConnectClient createKafkaConnectClient(String address) {
		return Feign.builder().contract(new SpringMvcContract()).encoder(encoder).decoder(decoder)
				.target(KafkaConnectClient.class, address);
	}

	/**
	 * Create admin client from broker list.
	 *
	 * @param brokers the broker list
	 * @return the admin client
	 */
	protected Properties getConnection(List<String> brokers) {
		// Check brokers
		if (CollectionUtils.isEmpty(brokers)) {
			return null;
		}

		// Set properties
		String brokerList = String.join(",", brokers);
		log.debug("Create admin client from {}", brokerList);
		Properties props = new Properties();
		props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokerList);
		return props;
	}

	/**
	 * Check kafka connexion.
	 * 
	 * @param brokers the broker list
	 */
	public void checkKafka(List<String> brokers) {
		AdminClient adminClient = AdminClient.create(getConnection(brokers));
		try {
			adminClient.describeCluster().controller().get().host();
			adminClient.close();
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka " + brokers, e);
		}
	}

}
