package com.worldline.kafka.kafkamanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.producteur.ProducerMessagesDto;
import com.worldline.kafka.kafkamanager.dto.producteur.ProducerRecordDto;
import com.worldline.kafka.kafkamanager.service.ProducerService;

@RestController
@RequestMapping("/clusters/{clusterId}/producers")
public class ProducerController {

	@Autowired
	private ProducerService producerService;

	@Secured("ADMIN")
	@PostMapping("/{topicName}")
	public ResponseEntity<List<ProducerRecordDto>> sendMessagesToTopic(@PathVariable("clusterId") String clusterId,
			@PathVariable("topicName") String topicName, @Valid @RequestBody ProducerMessagesDto messages) {
		return ResponseEntity.ok(producerService.sendMessagesToTopic(clusterId, topicName, messages));
	}

}
