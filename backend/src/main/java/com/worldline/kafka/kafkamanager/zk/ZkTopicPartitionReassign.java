package com.worldline.kafka.kafkamanager.zk;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * ZK Topic partition reassign.
 */
@Data
public class ZkTopicPartitionReassign implements Serializable {

	private static final long serialVersionUID = -7297802354210181583L;

	private String version;

	private List<ZkTopicPartitionAssignment> partitions;

}
