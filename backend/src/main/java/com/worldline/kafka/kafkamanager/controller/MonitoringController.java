package com.worldline.kafka.kafkamanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.dto.monitoring.MonitoringResponseDto;
import com.worldline.kafka.kafkamanager.dto.monitoring.MonitoringSearchDto;
import com.worldline.kafka.kafkamanager.service.MonitoringService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;

/**
 * Metrics controller.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/monitoring")
@ConditionalOnProperty(value = "monitoring.enable")
public class MonitoringController {

	private final MonitoringService monitoringService;

	/**
	 * Controller.
	 * 
	 * @param monitoringService the monitoring service
	 */
	@Autowired
	public MonitoringController(MonitoringService monitoringService) {
		this.monitoringService = monitoringService;
	}

	@GetMapping("/activity")
	@ActivityEvent(value = EventType.MONITORING_ACTIVITY, response = "body.status == 'UP'", onlyKo = true)
	public ResponseEntity<MonitoringResponseDto> checkActivity(@PathVariable("clusterId") String clusterId,
			@Valid MonitoringSearchDto request) {
		return ResponseEntity.ok(monitoringService.checkActivity(clusterId, request));
	}

	@GetMapping("/lag")
	@ActivityEvent(value = EventType.MONITORING_LAG, response = "body.status == 'UP'", onlyKo = true)
	public ResponseEntity<MonitoringResponseDto> checkLag(@PathVariable("clusterId") String clusterId,
			@Valid MonitoringSearchDto request) {
		if (request.getLagThreshold() == null) {
			return new ResponseEntity("The LagThreshold field must be define", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(monitoringService.checkLag(clusterId, request));
	}

	@GetMapping("/group-consumer/servers/{nbServer}")
	@ActivityEvent(value = EventType.MONITORING_NB_SERVER, response = "body.status == 'UP'", onlyKo = true)
	public ResponseEntity<MonitoringResponseDto> checkNbServers(@PathVariable("clusterId") String clusterId,
			@PathVariable("nbServer") long nbServer, @Valid MonitoringSearchDto request) {
		if (request.getGroupIds().size() != 1) {
			return new ResponseEntity("Only one groupdId must be define", HttpStatus.BAD_REQUEST);
		}
		if (nbServer < 0) {
			return new ResponseEntity("The number of server must be a positive value", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(monitoringService.checkNbServers(clusterId, nbServer, request));
	}

	@GetMapping("/no-activity")
	@ActivityEvent(value = EventType.MONITORING_NO_ACTIVITY, response = "body.status == 'UP'", onlyKo = true)
	public ResponseEntity<MonitoringResponseDto> checkNoActivity(@PathVariable("clusterId") String clusterId,
			@Valid MonitoringSearchDto request) {
		return ResponseEntity.ok(monitoringService.checkNoActivity(clusterId, request));
	}

}
