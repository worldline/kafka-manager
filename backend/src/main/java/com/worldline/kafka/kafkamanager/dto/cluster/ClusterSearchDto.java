package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;

import lombok.Data;

/**
 * Cluster search dto.
 */
@Data
public class ClusterSearchDto implements Serializable {

	private static final long serialVersionUID = 9084912702703148763L;

	private String name;

	private String kafkaVersion;

	private boolean full;
}
