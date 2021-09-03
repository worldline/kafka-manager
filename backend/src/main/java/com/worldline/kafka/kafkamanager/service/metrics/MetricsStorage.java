package com.worldline.kafka.kafkamanager.service.metrics;

import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;
import com.worldline.kafka.kafkamanager.model.Metric;
import java.util.List;

/**
 * Metrics storage.
 */
public interface MetricsStorage {

	/**
	 * Save entities.
	 * 
	 * @param entities the entities
	 */
	void save(List<Metric> entities);

	/**
	 * Save entity.
	 * 
	 * @param entity the entity
	 */
	void save(Metric entity);

	/**
	 * Search metrics.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @return the metric list
	 */
	List<Metric> search(String clusterId, MetricSearchDto request);

	/**
	 * Last metric.
	 * 
	 * @param clusterId the cluster ID
	 * @param request	the search request
	 * @return an Optional metric
	 */
	List<Metric> last(String clusterId, MetricSearchDto request);
}
