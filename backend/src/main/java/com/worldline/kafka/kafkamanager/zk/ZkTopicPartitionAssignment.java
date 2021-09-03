package com.worldline.kafka.kafkamanager.zk;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * ZK Topic partition assignment.
 */
@Data
public class ZkTopicPartitionAssignment implements Serializable {

	private static final long serialVersionUID = -2011423568346054160L;

	private String topic;

	private int partition;

	private List<Integer> replicas;

}
