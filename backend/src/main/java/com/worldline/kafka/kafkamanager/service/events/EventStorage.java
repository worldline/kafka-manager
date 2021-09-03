package com.worldline.kafka.kafkamanager.service.events;

import java.util.List;

import com.worldline.kafka.kafkamanager.dto.event.EventSearchDto;
import com.worldline.kafka.kafkamanager.model.Event;

/**
 * Event storage.
 */
public interface EventStorage {

	/**
	 * Save entity.
	 * 
	 * @param entity the entity
	 */
	void save(Event entity);

	/**
	 * Search events.
	 * 
	 * @param request the search request
	 * @return the events
	 */
	List<Event> search(EventSearchDto request);
}
