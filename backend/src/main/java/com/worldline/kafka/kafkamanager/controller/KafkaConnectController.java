package com.worldline.kafka.kafkamanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorCreationDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectConnectorUpdateDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginValidationRequestDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectPluginValidationResponseDto;
import com.worldline.kafka.kafkamanager.dto.kafkaconnect.KafkaConnectVersionDto;
import com.worldline.kafka.kafkamanager.service.KafkaConnectService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

/**
 * Kafka connect controller.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/kafka-connect")
public class KafkaConnectController {

	private KafkaConnectService kafkaConnectService;

	/**
	 * Contructor.
	 * 
	 * @param kafkaConnectService the kafka connect service
	 */
	@Autowired
	public KafkaConnectController(KafkaConnectService kafkaConnectService) {
		this.kafkaConnectService = kafkaConnectService;
	}

	@GetMapping
	public ResponseEntity<KafkaConnectVersionDto> getVersion(@PathVariable("clusterId") String clusterId) {
		return ResponseEntity.ok(kafkaConnectService.getVersion(clusterId));
	}

	@GetMapping("/connectors")
	public ResponseEntity<Page<KafkaConnectConnectorDto>> getConnectors(@PathVariable("clusterId") String clusterId,
			Pageable pageable) {
		return ResponseEntity.ok(kafkaConnectService.getConnectors(clusterId, pageable));
	}

	@GetMapping("/connectors/{connectorId}")
	public ResponseEntity<KafkaConnectConnectorDto> getConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		return ResponseEntity.ok(kafkaConnectService.getConnector(clusterId, connectorId));
	}

	@Secured("ADMIN")
	@PutMapping("/connectors/{connectorId}/config")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_UPDATE, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void updateConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId,
			@RequestBody @Valid KafkaConnectConnectorUpdateDto request) {
		kafkaConnectService.updateConnector(clusterId, connectorId, request);
	}

	@Secured("ADMIN")
	@PostMapping("/connectors")
	@ActivityEvent(value = EventType.CONNECTOR_CREATE, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#request.name") })
	public ResponseEntity<KafkaConnectConnectorDto> createConnector(@PathVariable("clusterId") String clusterId,
			@RequestBody @Valid KafkaConnectConnectorCreationDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(kafkaConnectService.createConnector(clusterId, request));
	}

	@PostMapping("/connectors/{connectorId}/restart")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_RESTART, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void restartConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		kafkaConnectService.restartConnector(clusterId, connectorId);
	}

	@PutMapping("/connectors/{connectorId}/pause")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_PAUSE, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void pauseConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		kafkaConnectService.pauseConnector(clusterId, connectorId);
	}

	@PutMapping("/connectors/{connectorId}/resume")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_RESUME, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void resumeConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		kafkaConnectService.resumeConnector(clusterId, connectorId);
	}

	@Secured("ADMIN")
	@DeleteMapping("/connectors/{connectorId}")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_DELETE, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void deleteConnector(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		kafkaConnectService.deleteConnector(clusterId, connectorId);
	}

	@GetMapping("/connector-plugins")
	public ResponseEntity<Page<KafkaConnectPluginDto>> getPlugins(@PathVariable("clusterId") String clusterId,
			Pageable pageable) {
		return ResponseEntity.ok(kafkaConnectService.getPlugins(clusterId, pageable));
	}

	@PutMapping("/connector-plugins/{pluginName}/config/validate")
	public ResponseEntity<KafkaConnectPluginValidationResponseDto> validatePlugin(
			@PathVariable("clusterId") String clusterId, @PathVariable("pluginName") String pluginName,
			@RequestBody @Valid KafkaConnectPluginValidationRequestDto request) {
		return ResponseEntity.ok(kafkaConnectService.validatePlugin(clusterId, pluginName, request));
	}

	@Secured("ADMIN")
	@PutMapping("/connectors/{connectorId}/topics/reset")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_RESET_TOPICS, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId") })
	public void resetTopics(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId) {
		kafkaConnectService.resetTopics(clusterId, connectorId);
	}

	@Secured("ADMIN")
	@PostMapping("/connectors/{connectorId}/tasks/{taskId}/restart")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONNECTOR_TASK_RESTART, args = {
			@ActivityEventArg(key = EventArgs.CONNECTOR, value = "#connectorId"),
			@ActivityEventArg(key = EventArgs.CONNECTOR_TASK, value = "#taskId") })
	public void restartTask(@PathVariable("clusterId") String clusterId,
			@PathVariable("connectorId") String connectorId, @PathVariable("taskId") String taskId) {
		kafkaConnectService.restartTask(clusterId, connectorId, taskId);
	}

}
