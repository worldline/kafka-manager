package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Topic broker response dto.
 */
@Data
public class TopicBrokerResponseDto implements Serializable {

	private static final long serialVersionUID = 2791192211137455266L;

	private String id;

	private int nbPartitions;

	private int nbLeader;

	private List<Integer> partitions;

	private boolean skewed;

	private boolean leaderSkewed;

}
