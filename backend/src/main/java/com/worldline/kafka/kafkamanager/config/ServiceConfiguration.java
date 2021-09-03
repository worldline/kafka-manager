package com.worldline.kafka.kafkamanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.worldline.kafka.kafkamanager.mapper.ClusterMapper;
import com.worldline.kafka.kafkamanager.properties.JmxProperties;
import com.worldline.kafka.kafkamanager.repository.ClusterRepository;
import com.worldline.kafka.kafkamanager.service.BatchConsumerOffsetService;
import com.worldline.kafka.kafkamanager.service.cluster.ClusterService;
import com.worldline.kafka.kafkamanager.service.cluster.RestrictedClusterService;
import com.worldline.kafka.kafkamanager.service.cluster.StdClusterService;
import com.worldline.kafka.kafkamanager.service.zookamix.RestrictedZooKaMixService;
import com.worldline.kafka.kafkamanager.service.zookamix.StdZooKaMixService;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * Service configuration.
 */
@Configuration
@Import(FeignClientsConfiguration.class)
public class ServiceConfiguration {

	@Autowired(required = false)
	private ClusterRepository clusterRepository;

	@Autowired
	private Decoder decoder;

	@Autowired
	private Encoder encoder;

	@Bean
	public ZooKaMixService zooKaMixService(@Value("${database.enable:true}") boolean databaseEnable,
			AppConfiguration appConfiguration, BatchConsumerOffsetService batchConsumerOffsetService,
			JmxProperties jmxProperties) {
		if (!databaseEnable) {
			return new RestrictedZooKaMixService(appConfiguration, batchConsumerOffsetService, jmxProperties, decoder,
					encoder);
		}
		return new StdZooKaMixService(clusterRepository, batchConsumerOffsetService, jmxProperties, decoder, encoder);
	}

	@Bean
	public ClusterService clusterService(@Value("${database.enable:true}") boolean databaseEnable,
			AppConfiguration appConfiguration, ClusterMapper clusterMapper, ZooKaMixService zooKaMixService) {
		if (!databaseEnable) {
			return new RestrictedClusterService(appConfiguration, clusterMapper);
		}
		return new StdClusterService(clusterRepository, clusterMapper, zooKaMixService);
	}

}
