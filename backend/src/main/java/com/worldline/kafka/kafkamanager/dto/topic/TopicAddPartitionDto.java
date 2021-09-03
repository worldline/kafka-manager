package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * Topic add partition dto.
 */
@Data
public class TopicAddPartitionDto implements Serializable {

	private static final long serialVersionUID = 6100843059141364504L;

	@Min(1)
	private int nbPartitions;

	private List<List<Integer>> newAssignments;

}
