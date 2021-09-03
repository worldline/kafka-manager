package com.worldline.kafka.kafkamanager.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.worldline.kafka.kafkamanager.dto.monitoring.MonitoringResponseDto;
import com.worldline.kafka.kafkamanager.dto.monitoring.MonitoringSearchDto;
import com.worldline.kafka.kafkamanager.mapper.MonitoringMapper;
import com.worldline.kafka.kafkamanager.model.Metric;
import com.worldline.kafka.kafkamanager.model.MetricOffset;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

/**
 * Monitoring service.
 */
@Slf4j
public class MonitoringService {

	private MetricsStorage metricsStorage;

	/**
	 * Constructor.
	 *
	 * @param metricsStorage the metrics storage
	 */
	@Autowired
	public MonitoringService(MetricsStorage metricsStorage) {
		this.metricsStorage = metricsStorage;
	}

	/**
	 * Check Kafka cluster activity
	 *
	 * @param clusterId the cluster id
	 * @param searchDto the monitoring search request
	 * @return a MonitoringResponseDto with status equals true if any activity is
	 *         detected
	 */
	public MonitoringResponseDto checkActivity(String clusterId, MonitoringSearchDto searchDto) {
		// Build a MonitoringResponseDto with the result
		Pair<Boolean, String> result = this.detectActivity(clusterId, searchDto);
		return MonitoringMapper.map(LocalDateTime.now(), result.getFirst(), result.getSecond());
	}

	/**
	 * Check Kafka cluster no activity
	 *
	 * @param clusterId the cluster id
	 * @param searchDto the monitoring search request
	 * @return a MonitoringResponseDto with status equals false if any activity is
	 *         detected
	 */
	public MonitoringResponseDto checkNoActivity(String clusterId, MonitoringSearchDto searchDto) {
		// Build a MonitoringResponseDto with the result
		return MonitoringMapper.map(LocalDateTime.now(), !this.detectActivity(clusterId, searchDto).getFirst(), this.detectActivity(clusterId, searchDto).getSecond());
	}

	/**
	 * Detect Kafka cluster activity
	 *
	 * @param clusterId the cluster id
	 * @param searchDto the monitoring search request
	 * @return a Pair with activity status and optional comment
	 */
	private Pair<Boolean, String> detectActivity(String clusterId, MonitoringSearchDto searchDto) {
		// Retrieve filtered metrics with a MonitoringSearchDto
		List<Metric> metrics = metricsStorage.last(clusterId, searchDto);

		// Check Activity by topic 
		Map<String, Pair<Boolean, String>> topicResults = metrics.stream()
			.collect(Collectors.groupingBy(Metric::getTopicName, Collectors.toList()))
			.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, e -> this.detectActivityTopic(e.getKey(), e.getValue())));

		log.info(topicResults.toString());
		
		return Pair.of(
			topicResults.entrySet().stream().allMatch(e -> e.getValue().getFirst()),
			topicResults.entrySet().stream().filter(e -> StringUtils.hasText(e.getValue().getSecond())).map(e -> new String("[" + e.getKey() + "] " +  e.getValue().getSecond())).collect(Collectors.joining(", "))
		);
	}

	/**
	 * Detect the activity of topic with a list of metric
	 * 
	 * @param topicName the topic name
	 * @param metrics   the list of metric
	 * 
	 * @return a Pair with activity status and optional comment
	 */
	private Pair<Boolean, String>  detectActivityTopic(String topicName, List<Metric> metrics) {
		// Reduce Metrics at the topicName
		List<Metric> topicMetrics = metrics.stream().filter(m -> m.getTopicName().equals(topicName)).collect(Collectors.toList());

		// Check Activity by groupId 
		Map<String, Pair<Boolean, String>> topicResults = metrics.stream()
			.map(m -> m.getOffsets())
			.flatMap(List::stream)
			.collect(Collectors.groupingBy(MetricOffset::getGroupId, Collectors.toList()))
			.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, e -> this.detectActivityGroupId(e.getKey(), e.getValue())));

		log.info(topicResults.toString());
		
		return Pair.of(
			topicResults.entrySet().stream().allMatch(e -> e.getValue().getFirst()),
			topicResults.entrySet().stream().filter(e -> StringUtils.hasText(e.getValue().getSecond())).map(e -> new String("[" + e.getKey() + "] " +  e.getValue().getSecond())).collect(Collectors.joining(", "))
		);
	}

	/**
	 * Detect the activity of topic with a list of metric
	 *
	 * @param groupIdName the groupId name
	 * @param metrics   the list of metric
	 *
	 * @return a Pair with activity status and optional comment
	 */
	private Pair<Boolean, String>  detectActivityGroupId(String groupIdName, List<MetricOffset> metrics) {

		boolean currentOffsetActivity = metrics.stream().map(m -> m.getCurrentOffset()).distinct().count() > 1;
		boolean endOffsetActivity = metrics.stream().map(m -> m.getEndOffset()).distinct().count() > 1;

		boolean activity = true;
		String comment = "";
		if(!currentOffsetActivity && endOffsetActivity) {
			activity = false;
			comment = "[" + groupIdName + "] No activity";
		}
		
		return Pair.of(activity, comment);
	}

	/**
	 * Check lag for a specific cluster and request
	 *
	 * @param clusterId the cluster id
	 * @param request   the search request
	 * @return a MonitoringResponseDto
	 */
	public MonitoringResponseDto checkLag(String clusterId, MonitoringSearchDto request) {
		List<Metric> metrics = metricsStorage.last(clusterId, request);

		// Determine the lagThreshold
		int lagThreshold = request.getLagThreshold() == null ? 0 : request.getLagThreshold();

		List<MetricOffset> offsets = metrics.stream().map(Metric::getOffsets).flatMap(List::stream)
				.filter(o -> (o.getEndOffset() - o.getCurrentOffset()) > lagThreshold).collect(Collectors.toList());

		return MonitoringMapper.map(LocalDateTime.now(), CollectionUtils.isEmpty(offsets));
	}

	/**
	 * Check the number of server for a specific cluster and request with a GroupId
	 * 
	 * @param clusterId
	 * @param request
	 * @return a MonitoringResponseDto
	 */
	public MonitoringResponseDto checkNbServers(String clusterId, long exceptedNbServer, MonitoringSearchDto request) {
		List<Metric> metrics = metricsStorage.last(clusterId, request);
		long nbServer = metrics.stream().map(m -> m.getOffsets()).flatMap(List::stream).map(o -> o.getServerName())
				.distinct().count();
		return MonitoringMapper.map(LocalDateTime.now(), exceptedNbServer == nbServer);
	}
}
