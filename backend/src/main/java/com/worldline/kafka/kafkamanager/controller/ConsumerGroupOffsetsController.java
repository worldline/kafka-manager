package com.worldline.kafka.kafkamanager.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupOffsetResetDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupTopicPartitionDto;
import com.worldline.kafka.kafkamanager.dto.consumergroupsoffset.ConsumerGroupOffsetDto;
import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.dto.kafka.common.OffsetAndMetadataDto;
import com.worldline.kafka.kafkamanager.service.ConsumerGroupOffsetService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

@RestController
@RequestMapping("/clusters/{clusterId}/consumer-groups-offset")
public class ConsumerGroupOffsetsController {

	private final ConsumerGroupOffsetService consumerGroupOffsetService;

	/**
	 * Constructor.
	 *
	 * @param consumerGroupOffsetService the consumer group offset service
	 */
	@Autowired
	public ConsumerGroupOffsetsController(ConsumerGroupOffsetService consumerGroupOffsetService) {
		this.consumerGroupOffsetService = consumerGroupOffsetService;
	}

	@GetMapping("/{groupId}")
	public ResponseEntity<ConsumerGroupOffsetDto> get(@PathVariable("clusterId") String clusterId,
			@PathVariable("groupId") String groupId) {
		return ResponseEntity.ok(consumerGroupOffsetService.get(clusterId, groupId));
	}

	@Secured("ADMIN")
	@DeleteMapping("/{groupId}")
	public void delete(@PathVariable("clusterId") String clusterId, @PathVariable("groupId") String groupId,
			@RequestBody List<ConsumerGroupTopicPartitionDto> topics) {
		consumerGroupOffsetService.delete(clusterId, groupId, topics);
	}

	@Secured("ADMIN")
	@PostMapping("/{groupId}/alter")
	public void alterConsumerGroup(@PathVariable("clusterId") String clusterId, @PathVariable("groupId") String groupId,
			@RequestBody Map<ConsumerGroupTopicPartitionDto, OffsetAndMetadataDto> mapToUpdate) {
		consumerGroupOffsetService.alterConsumerGroup(clusterId, groupId, mapToUpdate);
	}

	@Secured("ADMIN")
	@PutMapping("/{groupId}/reset")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CONSUMER_GROUP_RESET_OFFSET, args = {
			@ActivityEventArg(key = EventArgs.CONSUMER_GROUP, value = "#groupId") })
	public void resetConsumerGroupOffset(@PathVariable("clusterId") String clusterId,
			@PathVariable("groupId") String groupId, @Valid @RequestBody ConsumerGroupOffsetResetDto request) {
		consumerGroupOffsetService.resetOffset(clusterId, groupId, request);
	}

	@Secured("ADMIN")
	@PostMapping("/{sourceGroupId}/from/{targetGroupId}")
	@ResponseStatus(HttpStatus.OK)
	public void setGroupOffsetFromAnotherGroupOffset(@PathVariable String clusterId, @PathVariable String sourceGroupId,
			@PathVariable String targetGroupId, @RequestParam long offset) {
		consumerGroupOffsetService.setGroupOffsetFromAnotherGroupOffset(clusterId, "KM-" + sourceGroupId, targetGroupId,
				offset);
	}

}
