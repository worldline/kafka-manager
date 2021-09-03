package com.worldline.kafka.kafkamanager.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Elastic search model for metric.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "#{@environment.getProperty('elasticsearch.prefix')}-metrics-#{T(java.time.LocalDate).now().toString()}")
public class Metric extends ElasticData implements Serializable {

	private static final long serialVersionUID = -3224764298866151635L;

	private String clusterId;

	private String topicName;

	private double messagesPerSeconds;

	private double bytesInPerSeconds;

	private double bytesOutPerSeconds;

	private double bytesRejectedPerSeconds;

	private double failedFetchPerSeconds;

	private double failedProducePerSeconds;

	@Field(type = FieldType.Nested, includeInParent = true)
	private List<MetricOffset> offsets;

	@Override
	public String getKey() {
		return clusterId;
	}

}
