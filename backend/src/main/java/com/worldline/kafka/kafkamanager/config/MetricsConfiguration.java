package com.worldline.kafka.kafkamanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.worldline.kafka.kafkamanager.mapper.MetricsMapper;
import com.worldline.kafka.kafkamanager.service.ConsumerGroupService;
import com.worldline.kafka.kafkamanager.service.MetricsService;
import com.worldline.kafka.kafkamanager.service.PageableService;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorageCache;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

/**
 * Metrics configuration.
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "metrics.enable", matchIfMissing = false)
public class MetricsConfiguration {

	@Autowired
	private MetricsService metricsService;

	@Value("${metrics.schedule:true}")
	private boolean enableSchedule;

	@Bean
	public MetricsService metricsService(ZooKaMixService zooKaMixService, MetricsMapper metricsMapper,
			ConsumerGroupService consumerGroupService, PageableService pageableService, MetricsStorage metricsStorage) {
		return new MetricsService(zooKaMixService, metricsMapper, consumerGroupService, pageableService,
				metricsStorage);
	}

	@Scheduled(cron = "${metrics.cron-async-storage}")
	public void storeMetrics() {
		if (enableSchedule) {
			log.debug("Start - Store all metrics");
			metricsService.storeAll();
			log.debug("End - Store all metrics");
		}
	}

	@Bean
	@ConditionalOnMissingBean(MetricsStorage.class)
	public MetricsStorage metricsStorage(@Value("${metrics.clean.clean-time}") int cleanTime) {
		return new MetricsStorageCache(cleanTime);
	}

}
