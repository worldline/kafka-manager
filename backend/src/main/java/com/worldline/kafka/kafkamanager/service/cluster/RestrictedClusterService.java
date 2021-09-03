package com.worldline.kafka.kafkamanager.service.cluster;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.MethodNotAllowedException;

import com.worldline.kafka.kafkamanager.config.AppConfiguration;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterCreateDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterResponseDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterSearchDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterUpdateDto;
import com.worldline.kafka.kafkamanager.mapper.ClusterMapper;

/**
 * Restricted implemntation of Cluster service. <br>
 * (Only one instance of Cluster given at the startup of application).
 */
public class RestrictedClusterService implements ClusterService {

	private ClusterMapper clusterMapper;

	private AppConfiguration appConfiguration;

	/**
	 * Constructor.
	 * 
	 * @param appConfiguration the application configuration
	 * @param clusterMapper    the cluster mapper
	 */
	@Autowired
	public RestrictedClusterService(AppConfiguration appConfiguration, ClusterMapper clusterMapper) {
		this.clusterMapper = clusterMapper;
		this.appConfiguration = appConfiguration;
	}

	@Override
	public ClusterResponseDto create(ClusterCreateDto request) {
		throw new MethodNotAllowedException("Not allowed in restricted mode", null);
	}

	@Override
	public void update(String id, ClusterUpdateDto request) {
		throw new MethodNotAllowedException("Not allowed in restricted mode", null);
	}

	@Override
	public void delete(String id) {
		throw new MethodNotAllowedException("Not allowed in restricted mode", null);
	}

	@Override
	public ClusterResponseDto get(String id) {
		return clusterMapper.map(appConfiguration.getCluster());
	}

	@Override
	public Page<ClusterResponseDto> list(ClusterSearchDto request, Pageable pageable) {
		return new PageImpl<ClusterResponseDto>(Arrays.asList(clusterMapper.map(appConfiguration.getCluster())));
	}

}
