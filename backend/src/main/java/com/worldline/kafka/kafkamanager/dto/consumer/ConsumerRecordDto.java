package com.worldline.kafka.kafkamanager.dto.consumer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.worldline.kafka.kafkamanager.dto.record.RecordDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Consumer record dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumerRecordDto extends RecordDto implements Serializable {

	private static final long serialVersionUID = 2490235102322433113L;

	private String key;

	private String value;

	private Map<String, String> headers;

	/**
	 * Add header.
	 * 
	 * @param key   the key
	 * @param value the value
	 */
	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put(key, value);
	}
}
