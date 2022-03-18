package com.worldline.kafka.kafkamanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.worldline.kafka.kafkamanager.mapper.MetricsMapper;
import com.worldline.kafka.kafkamanager.service.ConsumerGroupService;
import com.worldline.kafka.kafkamanager.service.MetricsService;
import com.worldline.kafka.kafkamanager.service.PageableService;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorageCache;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

/**
 * Metrics configuration.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "metrics.enable", matchIfMissing = false)
public class MetricsConfiguration {

	@Bean
	public MetricsService metricsService(ZooKaMixService zooKaMixService, MetricsMapper metricsMapper,
			ConsumerGroupService consumerGroupService, PageableService pageableService, MetricsStorage metricsStorage,
			@Value("${metrics.schedule:true}") boolean enableSchedule) {
		return new MetricsService(zooKaMixService, metricsMapper, consumerGroupService, pageableService, metricsStorage,
				enableSchedule);
	}

	@Bean
	@ConditionalOnMissingBean(MetricsStorage.class)
	public MetricsStorage metricsStorage(@Value("${metrics.clean.clean-time}") int cleanTime) {
		return new MetricsStorageCache(cleanTime);
	}

}
