package com.worldline.kafka.kafkamanager.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

/**
 * Elastic data.
 */
@Data
public abstract class ElasticData implements Serializable {

	private static final long serialVersionUID = 7058007384535713261L;

	@Id
	private String id = UUID.randomUUID().toString();

	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
	private LocalDateTime creationDate = LocalDateTime.now();

	@Transient
	public abstract String getKey();

}
