package com.worldline.kafka.kafkamanager.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventResponseDto;
import com.worldline.kafka.kafkamanager.dto.event.EventSearchDto;
import com.worldline.kafka.kafkamanager.dto.event.EventStatus;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.mapper.EventMapper;
import com.worldline.kafka.kafkamanager.model.Event;
import com.worldline.kafka.kafkamanager.service.events.EventStorage;

/**
 * Event service.
 */
@Service
public class EventService {

	private EventStorage eventStorage;

	private EventMapper eventMapper;

	private PageableService pageableService;

	/**
	 * Constructor.
	 * 
	 * @param eventStorage the event storage
	 */
	@Autowired
	public EventService(EventStorage eventStorage, EventMapper eventMapper, PageableService pageableService) {
		this.eventStorage = eventStorage;
		this.eventMapper = eventMapper;
		this.pageableService = pageableService;
	}

	/**
	 * Create event.
	 * 
	 * @param type   the event type
	 * @param status the event status
	 * @param key    the argument key
	 * @param value  the argument value
	 */
	public void create(EventType type, EventStatus status, EventArgs key, String value) {
		create(type, status, Collections.singletonMap(key.name(), value));
	}

	/**
	 * Create event.
	 * 
	 * @param type   the event type
	 * @param status the event status
	 * @param args   the event arguments
	 */
	public void create(EventType type, EventStatus status, Map<String, String> args) {
		// Get current authenticated user if exists
		String owner = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			owner = context.getAuthentication().getName();
		}

		// Create event
		Event event = new Event(type, status, owner);
		if (args != null && !args.isEmpty()) {
			event.setArgs(args);
		}
		eventStorage.save(event);
	}

	/**
	 * 
	 * @param request
	 * @param pageable
	 * @return
	 */
	public Page<EventResponseDto> list(EventSearchDto request, Pageable pageable) {
		// Execute request
		List<Event> data = eventStorage.search(request);

		// Map to page
		List<EventResponseDto> metrics = data.stream().map(eventMapper::map).collect(Collectors.toList());
		return pageableService.createPage(metrics, pageable);
	}

}
