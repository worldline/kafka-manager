package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldline.kafka.kafkamanager.serializer.CustomLocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract metric response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractMetricResponseDto implements Serializable {

	private static final long serialVersionUID = -3385039939924138794L;

	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime date;

	@NotBlank
	private String clusterId;

}
