package com.worldline.kafka.kafkamanager.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * Event search dto.
 */
@Data
public class EventSearchDto implements Serializable {

	private static final long serialVersionUID = -4171029389553660529L;

	private List<EventType> types;

	private String clusterId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;

	@Min(value = 0)
	private int lastMinutes;

}
