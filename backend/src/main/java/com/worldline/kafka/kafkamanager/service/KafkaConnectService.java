package com.worldline.kafka.kafkamanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.client.KafkaConnectClient;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorCreationDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorUpdateDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginValidationRequestDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginValidationResponseDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectVersionDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectorStatus;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

/**
 * Kafka connect service.
 */
@Service
public class KafkaConnectService {

	private final ZooKaMixService zooKaMixService;
	private final PageableService pageableService;

	/**
	 * Constructor.
	 *
	 * @param zooKaMixService zooKaMixService
	 * @param pageableService the pageable service
	 */
	@Autowired
	public KafkaConnectService(ZooKaMixService zooKaMixService, PageableService pageableService) {
		this.zooKaMixService = zooKaMixService;
		this.pageableService = pageableService;
	}

	/**
	 * Get kafka connect version.
	 * 
	 * @param clusterId the cluster ID
	 * @return the kafka version
	 */
	public KafkaConnectVersionDto getVersion(String clusterId) {
		return getKafkaConnectClient(clusterId).getVersion();
	}

	/**
	 * Get kafka connect connectors.
	 * 
	 * @param clusterId the cluster ID
	 * @param pageable  the pageable data
	 * @return the connector list
	 */
	public Page<KafkaConnectConnectorDto> getConnectors(String clusterId, Pageable pageable) {
		// Get kafka connect client
		KafkaConnectClient kafkaConnectClient = getKafkaConnectClient(clusterId);

		// Get kafka connector data
		List<String> connectorNames = kafkaConnectClient.getConnectors();
		List<KafkaConnectConnectorDto> connectors = connectorNames.stream()
				.map(connectorId -> getConnector(kafkaConnectClient, connectorId)).collect(Collectors.toList());

		// Return result
		return pageableService.createPage(connectors, pageable);
	}

	/**
	 * Get connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 * @return the connector
	 */
	public KafkaConnectConnectorDto getConnector(String clusterId, String connectorId) {
		return getConnector(getKafkaConnectClient(clusterId), connectorId);
	}

	/**
	 * Get connector.
	 * 
	 * @param kafkaConnectClient the kafka connect client
	 * @param connectorId        the connector ID
	 * @return the connector
	 */
	protected KafkaConnectConnectorDto getConnector(KafkaConnectClient kafkaConnectClient, String connectorId) {
		// Get kafka data
		KafkaConnectConnectorDto connector = kafkaConnectClient.getConnector(connectorId);
		KafkaConnectConnectorDto connectorStatus = kafkaConnectClient.getConnectorStatus(connectorId);

		// Merge data
		connector.setConnector(connectorStatus.getConnector());
		connector.setTasks(connectorStatus.getTasks());
		if (connectorStatus.getTasks().stream().anyMatch(t -> t.getState().equals("FAILED"))) {
			connector.setStatus(KafkaConnectorStatus.DOWN);
		}
		return connector;
	}

	/**
	 * Create connector.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the request
	 * @return the created connector
	 */
	public KafkaConnectConnectorDto createConnector(String clusterId, KafkaConnectConnectorCreationDto request) {
		return getKafkaConnectClient(clusterId).createConnector(request);
	}

	/**
	 * Update connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 * @param request     the update request
	 */
	public void updateConnector(String clusterId, String connectorId, KafkaConnectConnectorUpdateDto request) {
		getKafkaConnectClient(clusterId).updateConnectorConfiguration(connectorId, request.getConfig());
	}

	/**
	 * Restart connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 */
	public void restartConnector(String clusterId, String connectorId) {
		getKafkaConnectClient(clusterId).restartConnector(connectorId);
	}

	/**
	 * Pause connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 */
	public void pauseConnector(String clusterId, String connectorId) {
		getKafkaConnectClient(clusterId).pauseConnector(connectorId);
	}

	/**
	 * Resume connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 */
	public void resumeConnector(String clusterId, String connectorId) {
		getKafkaConnectClient(clusterId).resumeConnector(connectorId);
	}

	/**
	 * Delete connector.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 */
	public void deleteConnector(String clusterId, String connectorId) {
		getKafkaConnectClient(clusterId).deleteConnector(connectorId);
	}

	/**
	 * Get kafka connect plugins.
	 * 
	 * @param clusterId the cluster ID
	 * @param pageable  the pageable data
	 * @return the plugin list
	 */
	public Page<KafkaConnectPluginDto> getPlugins(String clusterId, Pageable pageable) {
		List<KafkaConnectPluginDto> plugins = getKafkaConnectClient(clusterId).getPlugins();
		return pageableService.createPage(plugins, pageable);
	}

	/**
	 * Validate plugin.
	 * 
	 * @param clusterId  the cluster ID
	 * @param pluginName the plugin name
	 * @param request    the request
	 * @return the plugin validation
	 */
	public KafkaConnectPluginValidationResponseDto validatePlugin(String clusterId, String pluginName,
			KafkaConnectPluginValidationRequestDto request) {
		return getKafkaConnectClient(clusterId).validatePlugin(pluginName, request.getConfig());
	}

	/**
	 * Reset topics.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 */
	public void resetTopics(String clusterId, String connectorId) {
		getKafkaConnectClient(clusterId).resetTopics(connectorId);
	}

	/**
	 * Restart task.
	 * 
	 * @param clusterId   the cluster ID
	 * @param connectorId the connector ID
	 * @param taskId      the task ID
	 */
	public void restartTask(String clusterId, String connectorId, String taskId) {
		getKafkaConnectClient(clusterId).restartTask(connectorId, taskId);
	}

	/**
	 * Get kafka connect client.
	 * 
	 * @param clusterId the cluster ID
	 * @return the kafka connect client
	 */
	private KafkaConnectClient getKafkaConnectClient(String clusterId) {
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		return zooKaMixAdmin.getKafkaConnectClient();
	}

}
