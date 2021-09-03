package com.worldline.kafka.kafkamanager.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.broker.BrokerResponseDto;
import com.worldline.kafka.kafkamanager.dto.broker.BrokerSearchDto;
import com.worldline.kafka.kafkamanager.dto.broker.BrokerStatus;
import com.worldline.kafka.kafkamanager.dto.setting.ConfigurationUpdateDto;
import com.worldline.kafka.kafkamanager.exception.KafkaException;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

/**
 * Broker service.
 */
@Slf4j
@Service
public class BrokerService {

	private ZooKaMixService zooKaMixService;

	private PageableService pageableService;

	/**
	 * Constructor.
	 * 
	 * @param zooKaMixService the zookeeper kafka jmx service
	 * @param pageableService the pageable service
	 */
	@Autowired
	public BrokerService(ZooKaMixService zooKaMixService, PageableService pageableService) {
		this.zooKaMixService = zooKaMixService;
		this.pageableService = pageableService;
	}

	/**
	 * Get list of broker.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @param pageable  the pageable data
	 * @return the broker list
	 */
	public Page<BrokerResponseDto> list(String clusterId, BrokerSearchDto request, Pageable pageable) {
		List<BrokerResponseDto> result = list(clusterId, request.isFull());
		return pageableService.createPage(result, pageable);
	}

	/**
	 * Get list of broker.
	 * 
	 * @param clusterId the cluster ID
	 * @param isFull    {@code true} to have all data related to the broker
	 * @return the broker list
	 */
	private List<BrokerResponseDto> list(String clusterId, boolean isFull) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		try {
			// Get broker informations
			Collection<Node> nodes = zooKaMixAdmin.getAdminClient().describeCluster().nodes().get();
			List<BrokerResponseDto> result = nodes.stream().map(node -> createBrokerNode(node, BrokerStatus.UP))
					.collect(Collectors.toList());

			// If a full details has been requested
			if (isFull) {
				// Get topic and partition information
				Set<String> topicNames = zooKaMixAdmin.getTopicNames();
				Map<String, TopicDescription> topics = zooKaMixAdmin.getAdminClient().describeTopics(topicNames).all()
						.get();
				int nbTopics = topics.size();
				Map<Integer, Integer> nbLeaderPartitions = new HashMap<>();
				List<Node> brokers = new ArrayList<>();
				topics.values().stream().forEach(topic -> {
					topic.partitions().stream().forEach(p -> {
						List<Node> brokerNodes = new ArrayList<>(p.replicas());
						// Manage leader
						if (p.leader() != null) {
							int value = 0;
							if (nbLeaderPartitions.containsKey(p.leader().id())) {
								value = nbLeaderPartitions.get(p.leader().id());
							}
							nbLeaderPartitions.put(p.leader().id(), value + 1);
							brokerNodes.add(p.leader());
						}

						// Manage nodes
						brokerNodes.stream().forEach(r -> {
							if (!brokers.stream().anyMatch(b -> b.id() == r.id())
									&& !result.stream().anyMatch(b -> b.getId() == r.id())) {
								brokers.add(r);
							}
						});
					});
				});
				final int nbPartitions = nbLeaderPartitions.values().stream().reduce(0, Integer::sum);

				// Add missing broker
				brokers.stream().map(n -> createBrokerNode(n, BrokerStatus.DOWN)).forEach(result::add);

				// Assign data
				result.stream().forEach(d -> {
					d.setNbTopics(nbTopics);
					d.setNbPartitions(nbPartitions);
					d.setNbLeaderPartitions(nbLeaderPartitions.get(d.getId()));
					try {
						d.setConfigurationMap(zooKaMixAdmin.getBrokerConfig(String.valueOf(d.getId())));
					} catch (Throwable e) {
						log.error("Cannot find broker properties");
					}
				});
			}
			return result;
		} catch (InterruptedException | ExecutionException e) {
			throw new KafkaException("Cannot connect to kafka", e);
		}
	}

	/**
	 * Get broker data.
	 * 
	 * @param clusterId the cluster ID
	 * @param brokerId  the broker ID
	 * @return the topic data
	 */
	public BrokerResponseDto get(String clusterId, int brokerId) {
		List<BrokerResponseDto> result = list(clusterId, true);
		return result.stream().filter(b -> b.getId() == brokerId).findFirst().orElse(null);
	}

	/**
	 * Update topic configuration.
	 * 
	 * @param clusterId the cluster ID
	 * @param brokerId  the broker ID
	 * @param request   the update request
	 */
	public void updateConfiguration(String clusterId, String brokerId, ConfigurationUpdateDto request) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		if (request.getConfiguration() != null && request.getConfiguration().size() > 0) {
			try {
				zooKaMixAdmin.setBrokerConfig(brokerId, request.getConfigurationMap());
			} catch (InterruptedException e) {
				throw new KafkaException("Cannot connect to kafka", e);
			}
		}
	}

	/**
	 * Create broker node.
	 * 
	 * @param node   the node
	 * @param status the broker status
	 * @return the broker
	 */
	private BrokerResponseDto createBrokerNode(Node node, BrokerStatus status) {
		BrokerResponseDto data = new BrokerResponseDto();
		data.setId(node.id());
		data.setHost(node.host());
		data.setPort(node.port());
		data.setStatus(status);
		return data;
	}

}
