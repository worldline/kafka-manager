package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * Consumer group overview dto.
 */
@Data
public class ConsumerGroupOverviewDto implements Serializable {

	private static final long serialVersionUID = 6283031732359933773L;

	private String groupId;
	private String state;
	private Map<String, Long> topicsLag;

	public ConsumerGroupOverviewDto(String groupId, String state) {
		this.groupId = groupId;
		this.state = state;
		this.topicsLag = new HashMap<>();
	}

	/**
	 * The a topic to the map topicsLag with the associated lag as value.
	 */
	public void addTopicDetails(String topic, long lag) {
		topicsLag.put(topic, lag);
	}
}
