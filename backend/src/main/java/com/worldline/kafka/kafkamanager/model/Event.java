package com.worldline.kafka.kafkamanager.model;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventStatus;
import com.worldline.kafka.kafkamanager.dto.event.EventType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Elastic search model for event.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "#{@environment.getProperty('elasticsearch.prefix')}-events-#{T(java.time.LocalDate).now().toString()}")
public class Event extends ElasticData implements Serializable {

	public static final String EVENT_KEY = "event";

	private static final long serialVersionUID = -5294083028699316629L;

	/**
	 * Constructor.
	 * 
	 * @param type      the event type
	 * @param statusthe event status
	 * @param owner     the event owner
	 */
	public Event(EventType type, EventStatus status, String owner) {
		this.type = type;
		this.status = status;
		this.owner = owner;
	}

	private EventType type;

	private EventStatus status;

	private String owner;

	private Map<String, String> args;

	/**
	 * Get argument.
	 * 
	 * @param arg the argument key
	 * @return the argument value
	 */
	@Transient
	public String getArg(EventArgs arg) {
		if (args != null && args.containsKey(arg.name())) {
			return args.get(arg.name());
		}
		return null;
	}

	@Override
	public String getKey() {
		return EVENT_KEY;
	}

}
