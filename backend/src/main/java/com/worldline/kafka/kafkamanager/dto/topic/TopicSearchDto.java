package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;

import lombok.Data;

/**
 * Topic search dto.
 */
@Data
public class TopicSearchDto implements Serializable {

	private static final long serialVersionUID = -6541404060482297049L;

	private boolean full;
}
