package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metric offset dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricOffsetDto implements Serializable {

	private static final long serialVersionUID = -1375895609952561399L;

	private String groupId;

	private int partition;

	private long currentOffset;

	private long endOffset;

}
