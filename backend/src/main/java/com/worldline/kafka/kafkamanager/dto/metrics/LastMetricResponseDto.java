package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Metric response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LastMetricResponseDto extends AbstractMetricResponseDto implements Serializable {

	private static final long serialVersionUID = 5885510294922890846L;

	public LastMetricResponseDto(LocalDateTime date, String clusterId, List<MetricResponseDto> topicResponse) {
		super(date, clusterId);
		this.topicResponse = topicResponse;
	}

	@NotEmpty
	private List<MetricResponseDto> topicResponse;
}
