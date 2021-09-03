package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reset consumer group offset partition.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerGroupOffsetPartitionResetDto implements Serializable {

	private static final long serialVersionUID = 4331415875478521016L;

	@NotBlank
	private String topic;

	private int partition;

	private long offset;
}
