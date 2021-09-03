package com.worldline.kafka.kafkamanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.worldline.kafka.kafkamanager.service.events.EventStorage;
import com.worldline.kafka.kafkamanager.service.events.EventStorageCache;

/**
 * Event configuration.
 */
@Configuration
public class EventConfiguration {

	@Bean
	@ConditionalOnMissingBean(EventStorage.class)
	public EventStorage eventStorage(@Value("${event.clean.clean-time}") int cleanTime) {
		return new EventStorageCache(cleanTime);
	}

}
