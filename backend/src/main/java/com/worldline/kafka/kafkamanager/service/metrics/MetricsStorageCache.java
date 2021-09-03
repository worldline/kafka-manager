package com.worldline.kafka.kafkamanager.service.metrics;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;
import com.worldline.kafka.kafkamanager.model.Metric;
import com.worldline.kafka.kafkamanager.model.MetricOffset;
import com.worldline.kafka.kafkamanager.service.DataStorageCache;

/**
 * Cache implementation for {@link MetricsStorage}.
 */
@Slf4j
public class MetricsStorageCache extends DataStorageCache<Metric> implements MetricsStorage {

	private final Map<String, List<Metric>> storage = new HashMap<>();

	/**
	 * Constructor.
	 * 
	 * @param cleanTime the clean time
	 */
	@Autowired
	public MetricsStorageCache(@Value("${metrics.clean.clean-time}") int cleanTime) {
		super(cleanTime);
	}

	@Scheduled(cron = "${metrics.clean.cron-clean-storage}")
	private void executeClean() {
		clean();
	}

	@Override
	public List<Metric> search(String clusterId, MetricSearchDto request) {
		return get(clusterId).stream().filter(metric -> searchFilter(metric, request)).collect(Collectors.toList());
	}

	@Override
	public List<Metric> last(String clusterId, MetricSearchDto request) {
		// Metric Filter (TopicName)
		List<Metric> lastMetrics = get(clusterId).stream().filter(metric -> searchFilter(metric, request))
				.collect(Collectors.toList());
		
		if (CollectionUtils.isEmpty(lastMetrics)) {
			return lastMetrics;
		}

		// MetricOffset Filter (Partition and Consumer)
		lastMetrics.forEach(lastMetric -> {
			// Update MetricOffsets in Metric
			lastMetric.setOffsets(lastMetric.getOffsets().stream()
					.filter(metricOffset -> searchOffsetFilter(metricOffset, request)).collect(Collectors.toList()));
		});

		// Remove Topic without Offset
		List<Metric> result = lastMetrics.stream().filter(metric -> CollectionUtils.isNotEmpty(metric.getOffsets()))
			.collect(Collectors.toList());

		return result;
	}

	/**
	 * Search Offset Filter
	 * 
	 * @param metricOffset the metric offset
	 * @param request      the search request
	 * @return {@code true} if the metricOffset must be returned
	 */
	private boolean searchOffsetFilter(MetricOffset metricOffset, MetricSearchDto request) {
		boolean result = true;

		if (CollectionUtils.isNotEmpty(request.getPartitions())) {
			result &= request.getPartitions().contains(metricOffset.getPartition());
		}

		if (CollectionUtils.isNotEmpty(request.getPartitionsExcluded())) {
			result &= !request.getPartitionsExcluded().contains(metricOffset.getPartition());
		}

		if (CollectionUtils.isNotEmpty(request.getGroupIds())) {
			result &= request.getGroupIds().contains(metricOffset.getGroupId());
		}

		if (CollectionUtils.isNotEmpty(request.getGroupIdsExcluded())) {
			result &= !request.getGroupIdsExcluded().contains(metricOffset.getGroupId());
		}

		return result;
	}

	/**
	 * Search filter.
	 * 
	 * @param metric  the metric
	 * @param request the search request
	 * @return {@code true} if the metric must be returned
	 */
	private boolean searchFilter(Metric metric, MetricSearchDto request) {
		boolean result = true;
		if (CollectionUtils.isNotEmpty(request.getTopicNames())) {
			result &= request.getTopicNames().contains(metric.getTopicName());
		}
		if (CollectionUtils.isNotEmpty(request.getTopicNamesExcluded())) {
			result &= !request.getTopicNamesExcluded().contains(metric.getTopicName());
		}
		if (request.getStartDate() != null && request.getEndDate() != null) {
			result &= request.getStartDate().isBefore(metric.getCreationDate())
					&& request.getEndDate().isAfter(metric.getCreationDate());
		}
		if (request.getLastMinutes() > 0) {
			result &= LocalDateTime.now().minusMinutes(request.getLastMinutes()).isBefore(metric.getCreationDate());
		}
		return result;
	}

}
