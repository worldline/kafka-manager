package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;

import lombok.Data;

/**
 * Topic metrics redponse dto.
 */
@Data
public class TopicMetricsResponseDto implements Serializable {

	private static final long serialVersionUID = 3952074696968558099L;

	private TopicMetricsDetailsDto messagesPerSeconds;

	private TopicMetricsDetailsDto bytesInPerSeconds;

	private TopicMetricsDetailsDto bytesOutPerSeconds;

	private TopicMetricsDetailsDto bytesRejectedPerSeconds;

	private TopicMetricsDetailsDto failedFetchPerSeconds;

	private TopicMetricsDetailsDto failedProducePerSeconds;

}
