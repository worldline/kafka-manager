package com.worldline.kafka.kafkamanager.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.worldline.kafka.kafkamanager.model.BrokerAddress;
import com.worldline.kafka.kafkamanager.model.Cluster;

import lombok.Data;

/**
 * App configuration.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cluster")
public class AppConfiguration {

	private String name;

	private String zkAddr;

	private List<BrokerAddress> brokerAddrs;

	private String kafkaVersion;

	private String kafkaConnectAddr;

	public Cluster getCluster() {
		Cluster cluster = new Cluster();

		cluster.setId(UUID.nameUUIDFromBytes(this.name.getBytes(StandardCharsets.UTF_8)).toString());
		cluster.setName(this.name);
		cluster.setZkAddr(this.zkAddr);
		cluster.setBrokerAddrs(brokerAddrs);
		cluster.setKafkaVersion(this.kafkaVersion);
		cluster.setKafkaConnectAddr(this.kafkaConnectAddr);

		return cluster;
	}
}
