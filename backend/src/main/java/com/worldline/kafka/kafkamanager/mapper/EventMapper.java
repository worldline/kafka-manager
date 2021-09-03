package com.worldline.kafka.kafkamanager.mapper;

import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.event.EventResponseDto;
import com.worldline.kafka.kafkamanager.model.Event;

/**
 * Event mapper.
 */
@Service
public class EventMapper {

	/**
	 * Map {@link Event} to {@link EventResponseDto}.
	 * 
	 * @param event {@link Event}
	 * @return {@link EventResponseDto}
	 */
	public EventResponseDto map(Event data) {
		EventResponseDto result = new EventResponseDto();
		result.setDate(data.getCreationDate());
		result.setOwner(data.getOwner());
		result.setStatus(data.getStatus());
		result.setType(data.getType());
		result.setArgs(data.getArgs());
		return result;
	}
}
