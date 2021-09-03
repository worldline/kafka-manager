package com.worldline.kafka.kafkamanager.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.worldline.kafka.kafkamanager.model.Event;

/**
 * Event repository.
 */
public interface EventRepository extends ElasticsearchRepository<Event, String> {

}
