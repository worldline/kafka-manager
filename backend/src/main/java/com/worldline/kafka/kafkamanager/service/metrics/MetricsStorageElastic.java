package com.worldline.kafka.kafkamanager.service.metrics;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;
import com.worldline.kafka.kafkamanager.model.Metric;
import com.worldline.kafka.kafkamanager.repository.MetricRepository;

/**
 * Elastic implementation for {@link MetricsStorage}.
 */
public class MetricsStorageElastic implements MetricsStorage {

	private MetricRepository metricRepository;

	private ElasticsearchOperations elasticsearchOperations;

	private String elasticPrefix;

	/**
	 * Constructor.
	 * 
	 * @param metricRepository        the metric repository
	 * @param elasticsearchOperations the ES operations
	 * @param elasticPrefix           the elastic prefix
	 */
	@Autowired
	public MetricsStorageElastic(MetricRepository metricRepository, ElasticsearchOperations elasticsearchOperations,
			@Value("${elasticsearch.prefix}") String elasticPrefix) {
		this.metricRepository = metricRepository;
		this.elasticsearchOperations = elasticsearchOperations;
		this.elasticPrefix = elasticPrefix;
	}

	@Override
	public void save(List<Metric> entities) {
		checkIndex(Metric.class);
		metricRepository.saveAll(entities);
	}

	@Override
	public void save(Metric entity) {
		checkIndex(Metric.class);
		metricRepository.save(entity);
	}

	@Override
	public List<Metric> search(String clusterId, MetricSearchDto request) {
		// Create request
		Criteria criteria = new Criteria("clusterId").is(clusterId);
		if (CollectionUtils.isNotEmpty(request.getTopicNames())) {
			criteria = criteria.and(new Criteria("topicName").in(request.getTopicNames()));
		}
		if (CollectionUtils.isNotEmpty(request.getTopicNamesExcluded())) {
			criteria = criteria.and(new Criteria("topicName").notIn(request.getTopicNamesExcluded()));
		}
		if (request.getStartDate() != null && request.getEndDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
			criteria = criteria.and(new Criteria("creationDate").between(dateFormat.format(request.getStartDate()),
					dateFormat.format(request.getEndDate())));
		}
		if (request.getLastMinutes() > 0) {
			criteria = criteria.and(new Criteria("creationDate").greaterThan("now-" + request.getLastMinutes() + "m"));
		}
		Query searchQuery = new CriteriaQuery(criteria);

		// Get indexes
		List<String> indexes = new ArrayList<>();
		LocalDateTime startDate = (request.getStartDate() != null) ? request.getStartDate() : LocalDateTime.now();
		LocalDateTime endDate = (request.getEndDate() != null) ? request.getEndDate() : LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		do {
			indexes.add(elasticPrefix + "-metrics-" + startDate.format(formatter));
			startDate = startDate.plus(1, ChronoUnit.DAYS);
		} while (startDate.isBefore(endDate));

		// Execute request
		SearchHits<Metric> data = elasticsearchOperations.search(searchQuery, Metric.class,
				IndexCoordinates.of(indexes.toArray(new String[indexes.size()])));

		return data.stream().map(SearchHit::getContent).collect(Collectors.toList());
	}

	@Override
	public List<Metric> last(String clusterId, MetricSearchDto request) {
		// Create request
		Criteria criteria = new Criteria("clusterId").is(clusterId);
		if (CollectionUtils.isNotEmpty(request.getTopicNames())) {
			criteria = criteria.and(new Criteria("topicName").in(request.getTopicNames()));
		}
		if (CollectionUtils.isNotEmpty(request.getTopicNamesExcluded())) {
			criteria = criteria.and(new Criteria("topicName").notIn(request.getTopicNamesExcluded()));
		}
		if (CollectionUtils.isNotEmpty(request.getPartitions())) {
			criteria = criteria.and(new Criteria("offsets.partition").in(request.getPartitions()));
		}
		if (CollectionUtils.isNotEmpty(request.getPartitionsExcluded())) {
			criteria = criteria.and(new Criteria("offsets.partition").notIn(request.getPartitionsExcluded()));
		}
		if (CollectionUtils.isNotEmpty(request.getGroupIds())) {
			criteria = criteria.and(new Criteria("offsets.groupdId").in(request.getGroupIds()));
		}
		if (CollectionUtils.isNotEmpty(request.getGroupIdsExcluded())) {
			criteria = criteria.and(new Criteria("offsets.groupdId").notIn(request.getGroupIdsExcluded()));
		}

		Query searchQuery = new CriteriaQuery(criteria);
		searchQuery.addSort(Sort.by(Direction.DESC, "creationDate"));
		searchQuery.setPageable(PageRequest.of(0, 1));

		// Execute request
		SearchHits<Metric> data = elasticsearchOperations.search(searchQuery, Metric.class);

		return data.stream().map(SearchHit::getContent).collect(Collectors.toList());
	}

	/**
	 * Check elastic index.
	 * 
	 * @param <T>  the index type
	 * @param type the index type
	 */
	private <T> void checkIndex(Class<T> type) {
		IndexOperations indexOps = elasticsearchOperations.indexOps(type);
		if (!indexOps.exists()) {
			indexOps.create();
			indexOps.putMapping(type);
		}
	}

}
