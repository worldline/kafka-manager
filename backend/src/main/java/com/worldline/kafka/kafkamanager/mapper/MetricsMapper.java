package com.worldline.kafka.kafkamanager.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.consumergroups.ConsumerGroupDescriptionDto;
import com.worldline.kafka.kafkamanager.dto.metrics.LastMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricOffsetDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsDetailsDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsResponseDto;
import com.worldline.kafka.kafkamanager.model.Metric;
import com.worldline.kafka.kafkamanager.model.MetricOffset;

/**
 * Metrics mapper.
 */
@Service
public class MetricsMapper {

	/**
	 * Create {@link Metric} from {@link TopicMetricsDetailsDto}.
	 * 
	 * @param clusterId the cluster ID
	 * @param topicName the topic name
	 * @param metrics   the metrics
	 * @return the metric entity
	 */
	public Metric map(String clusterId, String topicName, TopicMetricsResponseDto metrics) {
		Metric metric = new Metric();
		metric.setClusterId(clusterId);
		metric.setTopicName(topicName);
		metric.setBytesInPerSeconds(
				metrics.getBytesInPerSeconds() == null ? 0 : metrics.getBytesInPerSeconds().getMeanRate());
		metric.setBytesOutPerSeconds(
				metrics.getBytesOutPerSeconds() == null ? 0 : metrics.getBytesOutPerSeconds().getMeanRate());
		metric.setBytesRejectedPerSeconds(
				metrics.getBytesRejectedPerSeconds() == null ? 0 : metrics.getBytesRejectedPerSeconds().getMeanRate());
		metric.setFailedFetchPerSeconds(
				metrics.getFailedFetchPerSeconds() == null ? 0 : metrics.getFailedFetchPerSeconds().getMeanRate());
		metric.setFailedProducePerSeconds(
				metrics.getFailedProducePerSeconds() == null ? 0 : metrics.getFailedProducePerSeconds().getMeanRate());
		metric.setMessagesPerSeconds(
				metrics.getMessagesPerSeconds() == null ? 0 : metrics.getMessagesPerSeconds().getMeanRate());
		return metric;
	}

	/**
	 * Map {@link ConsumerGroupDescriptionDto} to {@link MetricOffset}.
	 * 
	 * @param topic          topicName
	 * @param consumerGroups {@link ConsumerGroupDescriptionDto}
	 * @return {@link MetricOffset}
	 */
	public List<MetricOffset> map(String topic, List<ConsumerGroupDescriptionDto> consumerGroups) {
		if (!StringUtils.hasText(topic)) {
			return Collections.emptyList();
		}

		List<MetricOffset> result = new ArrayList<>();
		consumerGroups.stream().forEach(group -> {
			group.getMembers().stream().forEach(member -> {
				if (member != null && CollectionUtils.isNotEmpty(member.getTopicPartitions())) {
					member.getTopicPartitions().stream().filter(partition -> topic.equals(partition.getName()))
							.forEach(partition -> {
								MetricOffset metricOffset = new MetricOffset();
								metricOffset.setPartition(partition.getPartition());
								metricOffset.setGroupId(group.getGroupId());
								metricOffset.setServerName(member.getHost());
								if (partition.getOffsets() != null) {
									metricOffset.setCurrentOffset(partition.getOffsets().getCurrent());
									metricOffset.setEndOffset(partition.getOffsets().getEnd());
								}
								result.add(metricOffset);
							});
				}
			});
		});
		return result;
	}

	/**
	 * Map {@link Metric} to {@link MetricResponseDto}.
	 * 
	 * @param entity {@link Metric}
	 * @return {@link MetricResponseDto}
	 */
	public MetricResponseDto map(Metric entity) {
		MetricResponseDto data = new MetricResponseDto();
		data.setClusterId(entity.getClusterId());
		data.setDate(entity.getCreationDate());
		data.setTopicName(entity.getTopicName());
		data.setBytesInPerSeconds(entity.getBytesInPerSeconds());
		data.setBytesOutPerSeconds(entity.getBytesOutPerSeconds());
		data.setBytesRejectedPerSeconds(entity.getBytesRejectedPerSeconds());
		data.setFailedFetchPerSeconds(entity.getFailedFetchPerSeconds());
		data.setFailedProducePerSeconds(entity.getFailedProducePerSeconds());
		data.setMessagesPerSeconds(entity.getMessagesPerSeconds());
		if (entity.getOffsets() != null) {
			data.setOffsets(entity.getOffsets().stream().map(this::map).collect(Collectors.toList()));
		}
		return data;
	}

	/**
	 * Map {@link MetricOffset} to {@link MetricOffsetDto}.
	 * 
	 * @param entity {@link MetricOffset}
	 * @return {@link MetricOffsetDto}
	 */
	public MetricOffsetDto map(MetricOffset entity) {
		MetricOffsetDto data = new MetricOffsetDto();
		data.setCurrentOffset(entity.getCurrentOffset());
		data.setEndOffset(entity.getEndOffset());
		data.setGroupId(entity.getGroupId());
		data.setPartition(entity.getPartition());
		return data;
	}

	public LastMetricResponseDto map(List<Metric> metrics) {
		LastMetricResponseDto result = null;

		if (CollectionUtils.isNotEmpty(metrics)) {
			result = new LastMetricResponseDto(metrics.get(0).getCreationDate(), metrics.get(0).getClusterId(),
					metrics.stream().map(metric -> map(metric)).collect(Collectors.toList()));
		}

		return result;
	}
}
