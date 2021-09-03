package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Lag metric response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LagMetricResponseDto extends AbstractMetricResponseDto implements Serializable {

	private static final long serialVersionUID = 1314974230768016150L;

	public LagMetricResponseDto(LocalDateTime date, String clusterId, String lag) {
		super(date, clusterId);
		this.lag = lag;
	}

	@NotBlank
	private String lag;
}
