package com.worldline.kafka.kafkamanager.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.worldline.kafka.kafkamanager.repository.EventRepository;
import com.worldline.kafka.kafkamanager.repository.MetricRepository;
import com.worldline.kafka.kafkamanager.service.events.EventStorage;
import com.worldline.kafka.kafkamanager.service.events.EventStorageElastic;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorage;
import com.worldline.kafka.kafkamanager.service.metrics.MetricsStorageElastic;

/**
 * Elastic search configuration.
 */
@ConditionalOnProperty(value = "elasticsearch.enable", matchIfMissing = false)
@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfiguration {

	@Bean
	public RestHighLevelClient client(@Value("${elasticsearch.url}") String elasticUrl) {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(elasticUrl).build();
		return RestClients.create(clientConfiguration).rest();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate(@Value("${elasticsearch.url}") String elasticUrl) {
		return new ElasticsearchRestTemplate(client(elasticUrl));
	}

	@Bean
	public MetricsStorage metricsStorage(MetricRepository metricRepository,
			ElasticsearchOperations elasticsearchOperations, @Value("${elasticsearch.prefix}") String elasticPrefix) {
		return new MetricsStorageElastic(metricRepository, elasticsearchOperations, elasticPrefix);
	}

	@Bean
	public EventStorage eventStorage(EventRepository eventRepository, ElasticsearchOperations elasticsearchOperations,
			@Value("${elasticsearch.prefix}") String elasticPrefix) {
		return new EventStorageElastic(eventRepository, elasticsearchOperations, elasticPrefix);
	}

}
