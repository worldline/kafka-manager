package com.worldline.kafka.kafkamanager.service.zookamix;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.worldline.kafka.kafkamanager.config.AppConfiguration;
import com.worldline.kafka.kafkamanager.exception.ResourceNotFoundException;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.model.Cluster;
import com.worldline.kafka.kafkamanager.properties.JmxProperties;
import com.worldline.kafka.kafkamanager.service.BatchConsumerOffsetService;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Restricted implementation of Zookeeper, kafka, JMX service.
 */
public class RestrictedZooKaMixService extends ZooKaMixService {

	private AppConfiguration appConfiguration;

	/**
	 * Constructor.
	 * 
	 * @param appConfiguration the application configuration
	 * @param jmxProperties    the jmx properties
	 * @param encoder          the spring encoder
	 * @param decoder          the spring decoder
	 */
	@Autowired
	public RestrictedZooKaMixService(AppConfiguration appConfiguration,
			BatchConsumerOffsetService batchConsumerOffsetService, JmxProperties jmxProperties, Decoder decoder,
			Encoder encoder) {
		super(batchConsumerOffsetService, jmxProperties, decoder, encoder);
		this.appConfiguration = appConfiguration;
	}

	@Override
	public ZooKaMixAdmin getConnection(String clusterId) {
		if (!connections.containsKey(clusterId)) {
			connections.put(clusterId, createConnection(appConfiguration.getCluster()));
		}
		ZooKaMixAdmin connection = connections.get(clusterId);
		if (connection == null) {
			throw new ResourceNotFoundException("Cannot find connection for " + clusterId);
		}
		return connection;
	}

	@Override
	public List<ZooKaMixAdmin> getConnections() {
		Cluster cluster = appConfiguration.getCluster();

		if (!connections.containsKey(cluster.getId())) {
			connections.put(cluster.getId(), createConnection(cluster));
		}

		return Arrays.asList(connections.get(cluster.getId()));
	}

}
