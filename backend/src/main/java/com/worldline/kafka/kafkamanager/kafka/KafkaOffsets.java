package com.worldline.kafka.kafkamanager.kafka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * Kafka offsets.
 */
@Data
public class KafkaOffsets implements Serializable {

	private static final long serialVersionUID = -9088759650019073263L;

	private List<KafkaOffset> offsets = new ArrayList<>();

	/**
	 * Get offset.
	 * 
	 * @param topic     the topic
	 * @param partition the partition
	 * @param groupId   the group ID
	 * @return the offset
	 */
	public Optional<KafkaOffset> get(String topic, int partition, String groupId) {
		return offsets.stream().filter(
				o -> o.getGroup().equals(groupId) && o.getPartition() == partition && o.getTopic().equals(topic))
				.findFirst();
	}

	/**
	 * Get offsets by topic.
	 * 
	 * @return the offsets
	 */
	public Map<String, List<KafkaOffset>> getByTopic() {
		return offsets.stream().collect(Collectors.groupingBy(KafkaOffset::getTopic));
	}

}
