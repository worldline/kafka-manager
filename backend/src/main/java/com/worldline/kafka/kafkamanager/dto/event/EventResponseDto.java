package com.worldline.kafka.kafkamanager.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.worldline.kafka.kafkamanager.serializer.CustomLocalDateTimeSerializer;

import lombok.Data;

/**
 * Event response dto.
 */
@Data
public class EventResponseDto implements Serializable {

	private static final long serialVersionUID = -3024489984181622616L;

	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime date;

	private EventType type;

	private EventStatus status;

	private String owner;

	private Map<String, String> args;
}
