package com.worldline.kafka.kafkamanager.properties;

import java.util.Arrays;
import java.util.List;

/**
 * JMX type.
 */
public enum JmxType {

	VERSION("version"),
	METRICS_TOPIC_MESSAGES_PER_SECONDS("metrics-topic-messages-in-per-sec"),
	METRICS_TOPIC_BYTES_IN_PER_SECONDS("metrics-topic-bytes-in-per-sec"),
	METRICS_TOPIC_BYTES_OUT_PER_SECONDS("metrics-topic-bytes-out-per-sec"),
	METRICS_TOPIC_BYTES_REJECTED_PER_SECONDS("metrics-topic-bytes-rejected-per-sec"),
	METRICS_TOPIC_FAILED_FETCH_PER_SECONDS("metrics-topic-failed-fetch-req-per-sec"),
	METRICS_TOPIC_FAILED_PRODUCE_PER_SECONDS("metrics-topic-failed-produce-req-per-sec");

	private String type;

	/**
	 * Constructor.
	 * 
	 * @param type the type
	 */
	private JmxType(String type) {
		this.type = type;
	}

	/**
	 * Get type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get metrics types.
	 * 
	 * @return the metrics types
	 */
	public static List<JmxType> getMetricsTypes() {
		return Arrays.asList(JmxType.METRICS_TOPIC_MESSAGES_PER_SECONDS, JmxType.METRICS_TOPIC_BYTES_IN_PER_SECONDS,
				JmxType.METRICS_TOPIC_BYTES_OUT_PER_SECONDS, JmxType.METRICS_TOPIC_BYTES_REJECTED_PER_SECONDS,
				JmxType.METRICS_TOPIC_FAILED_FETCH_PER_SECONDS, JmxType.METRICS_TOPIC_FAILED_PRODUCE_PER_SECONDS);
	}

}
