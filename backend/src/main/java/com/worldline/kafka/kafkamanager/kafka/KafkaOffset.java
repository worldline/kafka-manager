package com.worldline.kafka.kafkamanager.kafka;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kafka offset.
 */
@Data
@NoArgsConstructor
public class KafkaOffset implements Serializable {

	private static final long serialVersionUID = 1754605352098845741L;

	private String topic;

	private int partition;

	private long offset;

	private long end;

	private String group;

	/**
	 * Constructor.
	 * 
	 * @param topic     the topic
	 * @param partition the partition
	 * @param group     the group
	 * @param offset    the offset
	 */
	public KafkaOffset(String topic, int partition, String group, long offset) {
		this.topic = topic;
		this.partition = partition;
		this.group = group;
		this.offset = offset;
	}

}
