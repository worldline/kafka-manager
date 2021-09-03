package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Topic metrics details dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicMetricsDetailsDto implements Serializable {

	private static final long serialVersionUID = 4859733793409055432L;

	private double meanRate;

	private double oneMinuteRate;

	private double fiveMinuteRate;

	private double fifteenMinuteRate;

}
