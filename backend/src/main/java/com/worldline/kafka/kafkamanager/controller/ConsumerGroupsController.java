package com.worldline.kafka.kafkamanager.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescribeSearchDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescriptionDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupOverviewDto;
import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.service.ConsumerGroupService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

@RestController
@RequestMapping("/clusters/{clusterId}/consumer-groups")
public class ConsumerGroupsController {

	private final ConsumerGroupService consumerGroupService;

	public ConsumerGroupsController(ConsumerGroupService consumerGroupService) {
		this.consumerGroupService = consumerGroupService;
	}

	@GetMapping
	public ResponseEntity<Page<ConsumerGroupOverviewDto>> list(@PathVariable("clusterId") String clusterId,
			Pageable pageable) {
		return ResponseEntity.ok(consumerGroupService.list(clusterId, pageable));
	}

	@GetMapping("/{consumerGroupId}")
	public ResponseEntity<ConsumerGroupDescriptionDto> get(@PathVariable("clusterId") String clusterId,
			@PathVariable("consumerGroupId") String consumerGroupId) {
		return ResponseEntity.ok(consumerGroupService.get(clusterId, consumerGroupId));
	}

	@GetMapping("/describes")
	public ResponseEntity<Page<ConsumerGroupDescriptionDto>> getGroupDescriptions(
			@PathVariable("clusterId") String clusterId, @Valid ConsumerGroupDescribeSearchDto request,
			Pageable pageable) {
		return ResponseEntity.ok(consumerGroupService.describesGroups(clusterId, request, pageable));
	}

	@Secured("ADMIN")
	@DeleteMapping
	@ActivityEvent(value = EventType.CONSUMER_GROUP_DELETE, args = {
			@ActivityEventArg(key = EventArgs.CONSUMER_GROUP, value = "#request.groupIds[0]") })
	public void delete(@PathVariable("clusterId") String clusterId, ConsumerGroupDescribeSearchDto request) {
		consumerGroupService.delete(clusterId, request.getGroupIds());
	}
}
