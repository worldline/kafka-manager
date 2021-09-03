package com.worldline.kafka.kafkamanager.mapper;

import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.worldline.kafka.kafkamanager.dto.cluster.ClusterBrokerAddressDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterCreateDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterResponseDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterUpdateDto;
import com.worldline.kafka.kafkamanager.model.BrokerAddress;
import com.worldline.kafka.kafkamanager.model.Cluster;

/**
 * Cluster mapper.
 */
@Service
public class ClusterMapper {

	/**
	 * Map {@link ClusterCreateDto} to {@link Cluster}.
	 * 
	 * @param data {@link ClusterCreateDto}
	 * @return {@link Cluster}
	 */
	public Cluster map(ClusterCreateDto data) {
		Cluster entity = new Cluster();
		entity.setName(data.getName());
		entity.setZkAddr(data.getZkAddr());
		if (CollectionUtils.isNotEmpty(data.getBrokerAddrs())) {
			entity.setBrokerAddrs(data.getBrokerAddrs().stream().map(this::map).collect(Collectors.toList()));
		}
		entity.setKafkaVersion(data.getKafkaVersion());
		entity.setComment(data.getComment());
		entity.setKafkaConnectAddr(data.getKafkaConnectAddr());
		return entity;
	}

	/**
	 * Map {@link Cluster} to {@link ClusterResponseDto}.
	 * 
	 * @param entity {@link Cluster}
	 * @return {@link ClusterResponseDto}
	 */
	public ClusterResponseDto map(Cluster entity) {
		ClusterResponseDto data = new ClusterResponseDto();
		data.setId(entity.getId());
		data.setCreationDate(entity.getCreationDate());
		data.setName(entity.getName());
		data.setZkAddr(entity.getZkAddr());
		if (CollectionUtils.isNotEmpty(entity.getBrokerAddrs())) {
			data.setBrokerAddrs(entity.getBrokerAddrs().stream().map(this::map).collect(Collectors.toList()));
		}
		data.setKafkaVersion(entity.getKafkaVersion());
		data.setComment(entity.getComment());
		data.setKafkaConnectAddr(entity.getKafkaConnectAddr());
		return data;
	}

	/**
	 * Map {@link ClusterUpdateDto} to {@link Cluster}.
	 * 
	 * @param data   {@link ClusterUpdateDto}
	 * @param entity {@link entity}
	 * @return {@link Cluster}
	 */
	public Cluster map(ClusterUpdateDto data, Cluster entity) {
		if (data.getName() != null) {
			data.getName().ifPresent(entity::setName);
		}
		if (data.getZkAddr() != null) {
			data.getZkAddr().ifPresent(entity::setZkAddr);
		}
		if (data.getBrokerAddrs() != null) {
			data.getBrokerAddrs().ifPresent(list -> {
				if (CollectionUtils.isNotEmpty(data.getBrokerAddrs().get())) {
					entity.setBrokerAddrs(
							data.getBrokerAddrs().get().stream().map(this::map).collect(Collectors.toList()));
				}
			});
		}
		if (data.getKafkaVersion() != null) {
			data.getKafkaVersion().ifPresent(entity::setKafkaVersion);
		}
		if (data.getComment() != null) {
			entity.setComment(data.getComment().orElse(null));
		}
		if (data.getKafkaConnectAddr() != null) {
			entity.setKafkaConnectAddr(data.getKafkaConnectAddr().orElse(null));
		}
		return entity;
	}

	/**
	 * Map {@link ClusterBrokerAddressDto} to {@link BrokerAddress}.
	 * 
	 * @param data {@link ClusterBrokerAddressDto}
	 * @return {@link BrokerAddress}
	 */
	public BrokerAddress map(ClusterBrokerAddressDto data) {
		BrokerAddress entity = new BrokerAddress();
		entity.setAddress(data.getAddress());
		entity.setJmxPort(data.getJmxPort());
		entity.setKafkaPort(data.getKafkaPort());
		return entity;
	}

	/**
	 * Map {@link BrokerAddress} to {@link ClusterBrokerAddressDto}.
	 * 
	 * @param entity {@link BrokerAddress}
	 * @return {@link ClusterBrokerAddressDto}
	 */
	public ClusterBrokerAddressDto map(BrokerAddress entity) {
		ClusterBrokerAddressDto data = new ClusterBrokerAddressDto();
		data.setAddress(entity.getAddress());
		data.setJmxPort(entity.getJmxPort());
		data.setKafkaPort(entity.getKafkaPort());
		return data;
	}

}
