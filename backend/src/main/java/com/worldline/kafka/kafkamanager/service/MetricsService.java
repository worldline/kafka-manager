package com.worldline.kafka.kafkamanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescribeSearchDto;
import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescriptionDto;
import com.worldline.kafka.kafkamanager.dto.metrics.LagMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.LastMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;
import com.worldline.kafka.kafkamanager.dto.metrics.NbServerMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsResponseDto;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.MetricsMapper;
import com.worldline.kafka.kafkamanager.model.Metric;
import com.worldline.kafka.kafkamanager.model.MetricOffset;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

/**
 * Metrics service.
 */
@Slf4j
public class MetricsService {

	private final String NO_METRIC = "No metric";

	private ZooKaMixService zooKaMixService;

	private MetricsMapper metricsMapper;

	private ConsumerGroupService consumerGroupService;

	private PageableService pageableService;

	private MetricsStorage metricsStorage;

	private boolean enableSchedule;

	/**
	 * Constructor.
	 * 
	 * @param zooKaMixService      the zookeeper kafka jmx service
	 * @param metricsMapper        the metrics mapper
	 * @param consumerGroupService the consumer group service
	 * @param metricsStorage       the metrics storage
	 * @param pageableService      the pageable service
	 * @param enableSchedule       {@code true if the schedule is enable}
	 */
	@Autowired
	public MetricsService(ZooKaMixService zooKaMixService, MetricsMapper metricsMapper,
			ConsumerGroupService consumerGroupService, PageableService pageableService, MetricsStorage metricsStorage,
			@Value("${metrics.schedule:true}") boolean enableSchedule) {
		this.zooKaMixService = zooKaMixService;
		this.metricsMapper = metricsMapper;
		this.consumerGroupService = consumerGroupService;
		this.pageableService = pageableService;
		this.metricsStorage = metricsStorage;
		this.enableSchedule = enableSchedule;
	}

	/**
	 * Store metrics.
	 */
	@Scheduled(cron = "${metrics.cron-async-storage}")
	public void storeMetrics() {
		if (enableSchedule) {
			log.debug("Start - Store all metrics");
			this.storeAll();
			log.debug("End - Store all metrics");
		}
	}

	/**
	 * Get metrics.
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @param pageable  the pageable data
	 * @return the metrics
	 */
	public Page<MetricResponseDto> list(String clusterId, MetricSearchDto request, Pageable pageable) {
		// Execute request
		List<Metric> data = metricsStorage.search(clusterId, request);

		// Map to page
		List<MetricResponseDto> metrics = data.stream().map(metricsMapper::map).collect(Collectors.toList());
		return pageableService.createPage(metrics, pageable);
	}

	/**
	 * Returns the current lag of partition
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @return the lag
	 */
	public LagMetricResponseDto getLag(String clusterId, MetricSearchDto request) {
		List<Metric> metrics = metricsStorage.last(clusterId, request);

		if (CollectionUtils.isEmpty(metrics)) {
			return new LagMetricResponseDto(LocalDateTime.now(), clusterId, NO_METRIC);
		}

		// Compute lag (returns the max lag of metrics)
		long computedLag = metrics.stream().map(m -> computeLagByMetricOffsets(m.getOffsets())).reduce(0L, Long::max);

		return new LagMetricResponseDto(LocalDateTime.now(), clusterId, String.valueOf(computedLag));
	}

	/**
	 * Returns the sum of lag for a MetricOffset list
	 * 
	 * @param offsets a MetricOffset list
	 * @return the sum of lag
	 */
	private long computeLagByMetricOffsets(List<MetricOffset> offsets) {
		return offsets.stream().map(o -> o.getEndOffset() - o.getCurrentOffset()).reduce(0L, Long::sum);
	}

	/**
	 * Get last metric offset for a specific cluster and topic information
	 * 
	 * @param clusterId the cluster id
	 * @param request   the search request
	 * @return a MetricOffset
	 */
	public LastMetricResponseDto getLastMetricOffset(String clusterId, MetricSearchDto request) {
		return metricsMapper.map(metricsStorage.last(clusterId, request));
	}

	/**
	 * Get topic metrics.
	 *
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 * @return the topic metrics
	 */
	public TopicMetricsResponseDto getTopicMetrics(String clusterId, String topicName) {
		// Get kafka admin client
		ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(clusterId);

		// Get metrics
		return zooKaMixAdmin.getMetricsTopic(topicName);
	}

	/**
	 * Returns the number of server for a GroupId
	 * 
	 * @param clusterId the cluster ID
	 * @param request   the search request
	 * @return the number of server
	 */
	public NbServerMetricResponseDto getNbServers(String clusterId, MetricSearchDto request) {
		List<Metric> metrics = metricsStorage.last(clusterId, request);
		long nbServer = metrics.stream().map(m -> m.getOffsets()).flatMap(List::stream).map(o -> o.getServerName())
				.distinct().count();
		return new NbServerMetricResponseDto(LocalDateTime.now(), clusterId, request.getGroupIds(), nbServer);
	}

	/**
	 * Store all metrics.
	 */
	public void storeAll() {
		// Get connections
		List<ZooKaMixAdmin> connections = zooKaMixService.getConnections();

		// Get metrics
		connections.stream().filter(ZooKaMixAdmin::isOpen).forEach(connection -> {
			try {
				// Get topic list
				Set<String> topicNames = connection.getTopicNames();
				if (CollectionUtils.isEmpty(topicNames)) {
					log.warn("No topic found for metrics");
					return;
				}

				// Get topic offset
				ConsumerGroupDescribeSearchDto request = new ConsumerGroupDescribeSearchDto();
				request.setTopicNames(new ArrayList<>(topicNames));
				request.setOffsets(true);
				List<ConsumerGroupDescriptionDto> consumerGroups = consumerGroupService
						.getConsumerGroups(connection.getClusterId(), request);

				List<Metric> entities = topicNames.stream().map(topicName -> {
					// Map JMX Metrics
					TopicMetricsResponseDto metrics = connection.getMetricsTopic(topicName);
					Metric metric = metricsMapper.map(connection.getClusterId(), topicName, metrics);

					// Map offset metrics
					List<MetricOffset> metricOffsets = metricsMapper.map(topicName, consumerGroups);
					metric.setOffsets(metricOffsets);

					return metric;
				}).filter(Objects::nonNull).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(entities)) {
					metricsStorage.save(entities);
				}
			} catch (InterruptedException e) {
				// Don't throw exception to avoid stopping the process for all clusters / topics
				log.error("Cannot get metrics", e);
			}
		});
	}

}
