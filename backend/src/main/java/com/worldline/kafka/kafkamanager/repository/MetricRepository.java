package com.worldline.kafka.kafkamanager.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.worldline.kafka.kafkamanager.model.Metric;

/**
 * Metric repository.
 */
public interface MetricRepository extends ElasticsearchRepository<Metric, String> {

	List<Metric> findByTopicName(String topicName);

}
