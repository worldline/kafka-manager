package com.worldline.kafka.kafkamanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.EmbeddedHeaderUtils;
import org.springframework.cloud.stream.binder.MessageValues;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.producteur.ProducerMessageDto;
import com.worldline.kafka.kafkamanager.dto.producteur.ProducerMessagesDto;
import com.worldline.kafka.kafkamanager.dto.producteur.ProducerRecordDto;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.ProducerRecordMapper;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProducerService {

	private final ZooKaMixService zooKaMixService;
	private final ProducerRecordMapper producerRecordMapper;

	@Autowired
	public ProducerService(ZooKaMixService zooKaMixService, ProducerRecordMapper producerRecordMapper) {
		this.zooKaMixService = zooKaMixService;
		this.producerRecordMapper = producerRecordMapper;
	}

	/**
	 * Send a list of messages to the provided topic name. For each record, the
	 * offset, timestamp and partition number where the message was pushed will be
	 * returned.
	 * 
	 * @param clusterId Cluster id to connect to.
	 * @param topicName Topic name to push messages
	 * @param messages  List of messages to push
	 * @return Result for each successful record pused.
	 */
	public List<ProducerRecordDto> sendMessagesToTopic(String clusterId, String topicName,
			ProducerMessagesDto messages) {
		List<ProducerRecordDto> results = new ArrayList<>();
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);
		try (KafkaProducer<byte[], byte[]> producer = zooKaMixAdmin.createProducer()) {
			boolean insideHeader = false;
			for (ProducerMessageDto message : messages.getMessages()) {
				try {
					results.add(sendMessageToTopic(producer, message, topicName, insideHeader));
				} catch (IllegalArgumentException ex) {
					if (!insideHeader) {
						insideHeader = true;
						results.add(sendMessageToTopic(producer, message, topicName, true));
					} else {
						throw ex;
					}
				}
			}
		}
		return results;
	}

	/**
	 * Send message to topic.
	 * 
	 * @param producer     the producer
	 * @param message      the message to send
	 * @param topicName    the topic name
	 * @param insideHeader {@code true} if the headers must be set inside the
	 *                     message
	 * @return the produced message
	 */
	private ProducerRecordDto sendMessageToTopic(KafkaProducer<byte[], byte[]> producer, ProducerMessageDto message,
			String topicName, boolean insideHeader) {
		// Create key
		String key = StringUtils.hasText(message.getKey()) ? message.getKey() : "0";

		// Get message
		byte[] messageValue = message.getMessage().getBytes();

		// Set headers
		if (insideHeader && message.getHeaders() != null && message.getHeaders().size() > 0) {
			MessageValues values = new MessageValues(
					MessageBuilder.withPayload(message.getMessage().getBytes()).build());
			List<String> headers = new ArrayList<>();
			message.getHeaders().entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
				values.put(e.getKey(), e.getValue());
				headers.add(e.getKey());
			});
			messageValue = EmbeddedHeaderUtils.embedHeaders(values, headers.toArray(new String[1]));
		}

		// Create message
		ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topicName, message.getPartition(), key.getBytes(),
				messageValue);

		// Set headers
		if (!insideHeader && message.getHeaders() != null && message.getHeaders().size() > 0) {
			message.getHeaders().entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
				record.headers().add(e.getKey(), e.getValue().getBytes());
			});
		}

		// Send message
		try {
			RecordMetadata metadata = producer.send(record).get();
			return producerRecordMapper.map(metadata);
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error while sending record to [{}] with key [{}] and data [{}]", topicName, key, message);
		}
		return null;
	}
}
