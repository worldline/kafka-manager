package com.worldline.kafka.kafkamanager.service;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerCriteriaDto;
import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerRecordDto;
import com.worldline.kafka.kafkamanager.dto.record.RecordToDeleteDto;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.ConsumerRecordMapper;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consumer service.
 */
@Slf4j
@Service
public class ConsumerService {

	private final ZooKaMixService zooKaMixService;

	private final ConsumerRecordMapper consumerRecordMapper;

	@Value("${kafka.consumerservice.poll.duration:500}")
	private long pollDuration;
	
	/**
	 * Constructor.
	 *
	 * @param zooKaMixService zooKaMixService
	 */
	@Autowired
	public ConsumerService(ZooKaMixService zooKaMixService, ConsumerRecordMapper consumerRecordMapper) {
		this.zooKaMixService = zooKaMixService;
		this.consumerRecordMapper = consumerRecordMapper;
	}

	public List<ConsumerRecordDto> readMessages(String clusterId, String topicName, ConsumerCriteriaDto consumerCriteria) {

		// Create kafka consumer
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		KafkaConsumer<byte[], byte[]> consumer = zooKaMixAdmin.createConsumer(
			byte[].class,
			"KM-" + consumerCriteria.getConsumerGroupId(),
			consumerCriteria.isFromBeginning() ? consumerCriteria.isFromBeginning() : null,
			consumerCriteria.getNumberOfMessages(),
			false,
			false
		);

		// Subscribe to consumer
		consumer.subscribe(Collections.singleton(topicName));

		// Poll records
		List<ConsumerRecordDto> responseDto = new ArrayList<>();
		try {
			consumer.poll(Duration.ofMillis(pollDuration)).forEach(record -> responseDto.add(consumerRecordMapper.map(record)));
		} finally {
			if (consumerCriteria.isCommit()) {
				consumer.commitSync();
			}
			consumer.close();
		}

		return responseDto;
	}

	/**
	 * Delete records in the topic from the first offset until the provided offset
	 * (provided offset not included in the delete).
	 *
	 * @param clusterId      Cluster id to connect to
	 * @param topicName      Topic name to delete record from
	 * @param recordToDelete List of partition number / offset to delete until.
	 */
	public void deleteRecords(String clusterId, String topicName, List<RecordToDeleteDto> recordToDelete) {
		Map<TopicPartition, RecordsToDelete> toDelete = new HashMap<>();

		recordToDelete.forEach(it -> toDelete.put(new TopicPartition(topicName, it.getPartition()),
			RecordsToDelete.beforeOffset(it.getOffsetToDeleteUntil())));

		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		zooKaMixAdmin.getAdminClient().deleteRecords(toDelete);

		// Todo: handle future
	}
}
