package com.worldline.kafka.kafkamanager.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Clastic search model for offset.
 */
@Data
public class MetricOffset implements Serializable {

	private static final long serialVersionUID = 6863283778255639817L;

	@NotBlank
	private String groupId;

	private int partition;

	private long currentOffset;

	private long endOffset;
	
	private String serverName;

}
