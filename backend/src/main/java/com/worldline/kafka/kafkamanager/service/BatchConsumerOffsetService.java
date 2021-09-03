package com.worldline.kafka.kafkamanager.service;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;

import kafka.common.OffsetAndMetadata;
import kafka.coordinator.group.GroupMetadataManager;
import kafka.coordinator.group.OffsetKey;
import lombok.extern.slf4j.Slf4j;

/**
 * Batch consumer offset service.
 */
@Service
@Slf4j
public class BatchConsumerOffsetService {

	private static final String OFFSET_TOPIC = "__consumer_offsets";
	public static final String OFFSET_CONSUMER_GROUP = "offset-consumer";

	/**
	 * Consume offset on older kafka version.
	 * 
	 * @param zooKaMixAdmin the connection
	 */
	@Async
	public void consumeOffset(ZooKaMixAdmin zooKaMixAdmin) {
		try (KafkaConsumer<Bytes, Bytes> consumer = zooKaMixAdmin.createConsumer(Bytes.class, OFFSET_CONSUMER_GROUP,
				false, 100, false, false)) {
			consumer.subscribe(Arrays.asList(OFFSET_TOPIC));
			log.debug("Start batch: {}", zooKaMixAdmin.isOpen());
			while (zooKaMixAdmin.isOpen()) {
				log.debug("Wait for consumeOffset on {}", zooKaMixAdmin.getClusterId());
				ConsumerRecords<Bytes, Bytes> consumerRecords = consumer.poll(Duration.ofMinutes(1));
				log.debug("Consumer records: {}", consumerRecords.count());
				if (consumerRecords.count() > 0) {
					consumerRecords.forEach(consumerRecord -> {
						try {
							byte[] key = consumerRecord.key().get();
							if (key != null) {
								Object obj = GroupMetadataManager.readMessageKey(ByteBuffer.wrap(key));
								if (obj instanceof OffsetKey) {
									OffsetKey offsetKey = (OffsetKey) obj;
									byte[] value = consumerRecord.value().get();
									OffsetAndMetadata offsetAndMetadata = GroupMetadataManager
											.readOffsetMessageValue(ByteBuffer.wrap(value));
									String topic = offsetKey.key().topicPartition().topic();
									if (!topic.startsWith("__")) {
										log.debug("Update consumer offset data for {} => {} / {}",
												zooKaMixAdmin.getClusterId(), offsetKey, offsetAndMetadata);
										zooKaMixAdmin.addConsumerOffset(offsetKey, offsetAndMetadata);
									}
								}
							}
						} catch (Exception exc) {
							log.error("Cannot get offset data", exc);
						}
					});
				}
			}
		}
	}

}
