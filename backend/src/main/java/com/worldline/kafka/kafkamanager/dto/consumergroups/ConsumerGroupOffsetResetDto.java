package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Reset consumer group offset.
 */
@Data
public class ConsumerGroupOffsetResetDto implements Serializable {

	private static final long serialVersionUID = -3914541173763292678L;

	@Size(min = 1)
	private List<@Valid ConsumerGroupOffsetPartitionResetDto> offsets;

}
