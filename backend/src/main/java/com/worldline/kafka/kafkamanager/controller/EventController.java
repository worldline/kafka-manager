package com.worldline.kafka.kafkamanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.event.EventResponseDto;
import com.worldline.kafka.kafkamanager.dto.event.EventSearchDto;
import com.worldline.kafka.kafkamanager.service.EventService;

/**
 * Event controller.
 */
@RestController
@RequestMapping("/events")
public class EventController {

	private EventService eventService;

	/**
	 * Constructor.
	 * 
	 * @param eventService the event service
	 */
	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public ResponseEntity<Page<EventResponseDto>> list(@Valid EventSearchDto request, Pageable pageable) {
		return ResponseEntity.ok(eventService.list(request, pageable));
	}

}
