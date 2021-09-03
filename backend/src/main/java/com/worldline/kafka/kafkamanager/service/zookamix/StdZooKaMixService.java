package com.worldline.kafka.kafkamanager.service.zookamix;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.worldline.kafka.kafkamanager.exception.ResourceNotFoundException;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.model.Cluster;
import com.worldline.kafka.kafkamanager.properties.JmxProperties;
import com.worldline.kafka.kafkamanager.repository.ClusterRepository;
import com.worldline.kafka.kafkamanager.service.BatchConsumerOffsetService;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Standard implementation of Zookeeper, kafka, JMX service.
 */
public class StdZooKaMixService extends ZooKaMixService {

	private ClusterRepository clusterRepository;

	/**
	 * Constructor.
	 * 
	 * @param clusterRepository the cluster repository
	 * @param jmxProperties     the jmx properties
	 * @param encoder           the spring encoder
	 * @param decoder           the spring decoder
	 */
	@Autowired
	public StdZooKaMixService(ClusterRepository clusterRepository,
			BatchConsumerOffsetService batchConsumerOffsetService, JmxProperties jmxProperties, Decoder decoder,
			Encoder encoder) {
		super(batchConsumerOffsetService, jmxProperties, decoder, encoder);
		this.clusterRepository = clusterRepository;
	}

	@Override
	public ZooKaMixAdmin getConnection(String clusterId) {
		if (!connections.containsKey(clusterId)) {
			Optional<Cluster> cluster = clusterRepository.findById(clusterId);
			connections.put(clusterId, cluster.isPresent() ? createConnection(cluster.get()) : null);
		}
		ZooKaMixAdmin connection = connections.get(clusterId);
		if (connection == null) {
			throw new ResourceNotFoundException("Cannot find connection for " + clusterId);
		}
		return connection;
	}

	@Override
	public List<ZooKaMixAdmin> getConnections() {
		List<Cluster> clusters = (List<Cluster>) clusterRepository.findAll();
		return clusters.stream().map(c -> {
			if (!connections.containsKey(c.getId())) {
				connections.put(c.getId(), createConnection(c));
			}
			return connections.get(c.getId());
		}).collect(Collectors.toList());
	}

}
