package com.worldline.kafka.kafkamanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.broker.BrokerResponseDto;
import com.worldline.kafka.kafkamanager.dto.broker.BrokerSearchDto;
import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.dto.setting.ConfigurationUpdateDto;
import com.worldline.kafka.kafkamanager.service.BrokerService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

/**
 * Broker controller.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/brokers")
public class BrokerController {

	private BrokerService brokerService;

	@Autowired
	public BrokerController(BrokerService brokerService) {
		this.brokerService = brokerService;
	}

	@GetMapping
	public ResponseEntity<Page<BrokerResponseDto>> list(@PathVariable("clusterId") String clusterId,
			@Valid BrokerSearchDto request, Pageable pageable) {
		return ResponseEntity.ok(brokerService.list(clusterId, request, pageable));
	}

	@GetMapping("/{brokerId}")
	public ResponseEntity<BrokerResponseDto> get(@PathVariable("clusterId") String clusterId,
			@PathVariable("brokerId") int brokerId) {
		BrokerResponseDto brokerResponseDto = brokerService.get(clusterId, brokerId);
		if (brokerResponseDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(brokerResponseDto);
	}

	@Secured("ADMIN")
	@PutMapping("/{brokerId}/settings")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.BROKER_SETTINGS_UPDATE, args = {
			@ActivityEventArg(key = EventArgs.BROKER, value = "#brokerId") })
	public void updateConfiguration(@PathVariable("clusterId") String clusterId,
			@PathVariable("brokerId") String brokerId, @Valid @RequestBody ConfigurationUpdateDto request) {
		brokerService.updateConfiguration(clusterId, brokerId, request);
	}

}
