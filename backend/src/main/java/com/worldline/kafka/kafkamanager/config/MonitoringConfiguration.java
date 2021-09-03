package com.worldline.kafka.kafkamanager.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.worldline.kafka.kafkamanager.service.MonitoringService;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;

/**
 * Monitoring configuration.
 */
@Configuration
@ConditionalOnProperty(value = "monitoring.enable")
public class MonitoringConfiguration {

	@Bean
	public MonitoringService monitoringService(MetricsStorage metricsStorage) {
		return new MonitoringService(metricsStorage);
	}

}