package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.worldline.kafka.kafkamanager.dto.consumer.ConsumerOffsetDto;

import lombok.Data;

/**
 * Topic partition consumer dto.
 */
@Data
public class ConsumerGroupTopicPartitionDto implements Serializable {

	private static final long serialVersionUID = -3949499903024993057L;

	@NotBlank
	private String name;

	private int partition;

	private ConsumerOffsetDto offsets;

}
