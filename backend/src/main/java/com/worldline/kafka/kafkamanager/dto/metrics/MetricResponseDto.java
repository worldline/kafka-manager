package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Metric response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetricResponseDto extends AbstractMetricResponseDto implements Serializable {

	private static final long serialVersionUID = -3385039939924138794L;

	@NotBlank
	private String topicName;

	private double messagesPerSeconds;

	private double bytesInPerSeconds;

	private double bytesOutPerSeconds;

	private double bytesRejectedPerSeconds;

	private double failedFetchPerSeconds;

	private double failedProducePerSeconds;

	private List<@Valid MetricOffsetDto> offsets;
}
