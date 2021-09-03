package com.worldline.kafka.kafkamanager.dto.monitoring;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldline.kafka.kafkamanager.serializer.CustomLocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metric response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringResponseDto implements Serializable {

	private static final long serialVersionUID = -3385039939924138794L;

	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime date;

	@NotBlank
	private String status;

	private String comment;
}
