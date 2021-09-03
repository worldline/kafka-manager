package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Topic creation dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicCreationDto extends TopicAbstractDto implements Serializable {

	private static final long serialVersionUID = 9078959941198333782L;

	private short replicationFactor;

}
