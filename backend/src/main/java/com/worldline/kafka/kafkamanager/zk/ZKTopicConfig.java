package com.worldline.kafka.kafkamanager.zk;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * ZK Topic config.
 */
@Data
public class ZKTopicConfig implements Serializable {

	private static final long serialVersionUID = -577167686575882859L;

	private int version;

	private Map<String, String> config;

}
