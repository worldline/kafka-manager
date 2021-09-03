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
import com.worldline.kafka.kafkamanager.dto.setting.ConfigurationUpdateDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicAddPartitionDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicCreationDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicReassignDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicSearchDto;
import com.worldline.kafka.kafkamanager.service.TopicService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;

/**
 * Topic controller.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/topics")
public class TopicController {

	private final TopicService topicService;

	/**
	 * Constructor.
	 * 
	 * @param topicService the topic service
	 */
	@Autowired
	public TopicController(TopicService topicService) {
		this.topicService = topicService;
	}

	@GetMapping
	public ResponseEntity<Page<TopicResponseDto>> list(@PathVariable("clusterId") String clusterId,
			@Valid TopicSearchDto request, Pageable pageable) {
		return ResponseEntity.ok(topicService.list(clusterId, request, pageable));
	}

	@GetMapping("/{topicName}")
	public ResponseEntity<TopicResponseDto> get(@PathVariable("clusterId") String clusterId,
			@PathVariable("topicName") String topicName) {
		TopicResponseDto topicResponseDto = topicService.get(clusterId, topicName);
		if (topicResponseDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(topicResponseDto);
	}

	@Secured("ADMIN")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ActivityEvent(value = EventType.TOPIC_CREATE, args = {
			@ActivityEventArg(key = EventArgs.TOPIC, value = "#request.name") })
	public void create(@PathVariable("clusterId") String clusterId, @Valid @RequestBody TopicCreationDto request) {
		topicService.createTopic(clusterId, request);
	}

	@Secured("ADMIN")
	@DeleteMapping("/{topicName}")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.TOPIC_DELETE, args = {
			@ActivityEventArg(key = EventArgs.TOPIC, value = "#topicName") })
	public void delete(@PathVariable("clusterId") String clusterId, @PathVariable("topicName") String topicName) {
		topicService.deleteTopic(clusterId, topicName);
	}

	@Secured("ADMIN")
	@PutMapping("/{topicName}/settings")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.TOPIC_SETTINGS_UPDATE, args = {
			@ActivityEventArg(key = EventArgs.TOPIC, value = "#topicName") })
	public void updateConfiguration(@PathVariable("clusterId") String clusterId,
			@PathVariable("topicName") String topicName, @Valid @RequestBody ConfigurationUpdateDto request) {
		topicService.updateConfiguration(clusterId, topicName, request);
	}

	@Secured("ADMIN")
	@PostMapping("/{topicName}/partitions")
	@ResponseStatus(HttpStatus.CREATED)
	@ActivityEvent(value = EventType.TOPIC_ADD_PARTITIONS, args = {
			@ActivityEventArg(key = EventArgs.TOPIC, value = "#topicName"),
			@ActivityEventArg(key = EventArgs.NUMBER, value = "#request.nbPartitions") })
	public void addPartition(@PathVariable("clusterId") String clusterId, @PathVariable("topicName") String topicName,
			@Valid @RequestBody TopicAddPartitionDto request) {
		topicService.addPartition(clusterId, topicName, request);
	}

	@Secured("ADMIN")
	@PutMapping("/{topicName}/partitions/reassign")
	@ActivityEvent(value = EventType.TOPIC_REASSIGN_PARTITIONS, args = {
			@ActivityEventArg(key = EventArgs.TOPIC, value = "#topicName") })
	public void reassign(@PathVariable("clusterId") String clusterId, @PathVariable("topicName") String topicName,
			@Valid @RequestBody TopicReassignDto request) {
		topicService.reassign(clusterId, request, topicName);
	}

}
