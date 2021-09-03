package com.worldline.kafka.kafkamanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerCriteriaDto;
import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerRecordDto;
import com.worldline.kafka.kafkamanager.dto.record.RecordToDeleteDto;
import com.worldline.kafka.kafkamanager.service.ConsumerService;

@RestController
@RequestMapping("/clusters/{clusterId}/consumers")
public class ConsumerController {

	private final ConsumerService consumerService;

	/**
	 * Constructor.
	 *
	 * @param consumerService the consumer service
	 */
	@Autowired
	public ConsumerController(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@GetMapping("/{topicName}")
	public List<ConsumerRecordDto> readMessages(@PathVariable String clusterId, @PathVariable String topicName,
			ConsumerCriteriaDto consumerCriteria) {
		return consumerService.readMessages(clusterId, topicName, consumerCriteria);
	}

	@Secured("ADMIN")
	@PostMapping("/{topicName}/delete-records")
	public void deleteRecords(@PathVariable String clusterId, @PathVariable String topicName,
			@RequestBody List<RecordToDeleteDto> recordToDelete) {
		consumerService.deleteRecords(clusterId, topicName, recordToDelete);
	}

}
