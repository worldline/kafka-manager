package com.worldline.kafka.kafkamanager.dto.partition;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Partition response dto.
 */
@Data
public class PartitionResponseDto implements Serializable {

	private static final long serialVersionUID = 8134526823356010158L;

	private int leader;

	private int partition;

	private List<String> isr;

	private List<String> replicas;

	private Long endOffset;

	private boolean prefreLeader;

	private boolean underReplicate;
}
