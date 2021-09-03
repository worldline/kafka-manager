package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Topic reassign partition dto.
 */
@Data
public class TopicReassignPartitionDto implements Serializable {

	private static final long serialVersionUID = -4165756396753715795L;

	private int partitionId;

	@Size(min = 1)
	private List<Integer> replicas;

}
