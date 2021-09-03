package com.worldline.kafka.kafkamanager.dto.monitoring;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Monitoring search dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MonitoringSearchDto extends MetricSearchDto implements Serializable {

	private static final long serialVersionUID = -6579264110129882826L;

	// Specific Monitoring Fields
	@Min(value = 5)
	private int lastMinutes = 5;

	private Integer lagThreshold;
}
