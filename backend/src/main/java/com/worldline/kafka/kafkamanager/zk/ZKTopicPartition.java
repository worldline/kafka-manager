package com.worldline.kafka.kafkamanager.zk;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * ZK Topic partition.
 */
@Data
public class ZKTopicPartition implements Serializable {

	private static final long serialVersionUID = 7445884655099927755L;

	private int version;

	private Map<String, List<Integer>> partitions;
}
