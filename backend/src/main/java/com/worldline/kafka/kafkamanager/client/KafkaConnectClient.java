package com.worldline.kafka.kafkamanager.client;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorCreationDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginValidationResponseDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectTaskDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectTaskStatusDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectTopicsDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectVersionDto;

public interface KafkaConnectClient {

	@GetMapping
	public KafkaConnectVersionDto getVersion();

	@GetMapping("/connectors")
	public List<String> getConnectors();

	@GetMapping("/connectors/{connectorId}")
	public KafkaConnectConnectorDto getConnector(@PathVariable("connectorId") String connectorId);

	@PostMapping("/connectors")
	public KafkaConnectConnectorDto createConnector(@RequestBody @Valid KafkaConnectConnectorCreationDto request);

	@PutMapping("/connectors/{connectorId}/config")
	public KafkaConnectConnectorDto updateConnectorConfiguration(@PathVariable("connectorId") String connectorId,
			@RequestBody Map<String, String> config);

	@GetMapping("/connectors/{connectorId}/status")
	public KafkaConnectConnectorDto getConnectorStatus(@PathVariable("connectorId") String connectorId);

	@PostMapping("/connectors/{connectorId}/restart")
	public void restartConnector(@PathVariable("connectorId") String connectorId);

	@PutMapping("/connectors/{connectorId}/pause")
	public void pauseConnector(@PathVariable("connectorId") String connectorId);

	@PutMapping("/connectors/{connectorId}/resume")
	public void resumeConnector(@PathVariable("connectorId") String connectorId);

	@DeleteMapping("/connectors/{connectorId}")
	public void deleteConnector(@PathVariable("connectorId") String connectorId);

	@GetMapping("/connectors/{connectorId}/tasks")
	public List<KafkaConnectTaskDto> getTasks(@PathVariable("connectorId") String connectorId);

	@GetMapping("/connectors/{connectorId}/tasks/{taskId}/status")
	public KafkaConnectTaskStatusDto getTask(@PathVariable("connectorId") String connectorId,
			@PathVariable("taskId") String taskId);

	@PostMapping("/connectors/{connectorId}/tasks/{taskId}/restart")
	public void restartTask(@PathVariable("connectorId") String connectorId, @PathVariable("taskId") String taskId);

	@GetMapping("/connectors/{connectorId}/topics")
	public Map<String, KafkaConnectTopicsDto> getTopics(@PathVariable("connectorId") String connectorId);

	@PutMapping("/connectors/{connectorId}/topics/reset")
	public void resetTopics(@PathVariable("connectorId") String connectorId);

	@GetMapping("/connector-plugins")
	public List<KafkaConnectPluginDto> getPlugins();

	@PutMapping("/connector-plugins/{pluginName}/config/validate")
	public KafkaConnectPluginValidationResponseDto validatePlugin(@PathVariable("pluginName") String pluginName,
			@RequestBody Map<String, String> config);

}
