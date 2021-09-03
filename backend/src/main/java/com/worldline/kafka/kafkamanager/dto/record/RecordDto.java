package com.worldline.kafka.kafkamanager.dto.record;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Record dto.
 */
@Data
@NoArgsConstructor
public abstract class RecordDto implements Serializable {

	private static final long serialVersionUID = 4719501041872318367L;

	private long offset;

	private long timestamp;

	private int partition;

}
