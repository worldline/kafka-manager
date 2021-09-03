package com.worldline.kafka.kafkamanager.service.cluster;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterCreateDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterResponseDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterSearchDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterUpdateDto;
import com.worldline.kafka.kafkamanager.exception.BadRequestException;
import com.worldline.kafka.kafkamanager.exception.ResourceNotFoundException;
import com.worldline.kafka.kafkamanager.kafka.ZooKaMixAdmin;
import com.worldline.kafka.kafkamanager.mapper.ClusterMapper;
import com.worldline.kafka.kafkamanager.model.Cluster;
import com.worldline.kafka.kafkamanager.model.QCluster;
import com.worldline.kafka.kafkamanager.repository.ClusterRepository;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

import lombok.extern.slf4j.Slf4j;

/**
 * Standard implementation of Cluster service with database service.
 */
@Slf4j
public class StdClusterService implements ClusterService {

	private ClusterRepository clusterRepository;

	private ClusterMapper clusterMapper;

	private ZooKaMixService zooKaMixService;

	/**
	 * Constructor.
	 * 
	 * @param clusterRepository the cluster repository
	 * @param clusterMapper     the cluster mapper
	 * @param zooKaMixService   the zookeeper kafka jmx service
	 */
	@Autowired
	public StdClusterService(ClusterRepository clusterRepository, ClusterMapper clusterMapper,
			ZooKaMixService zooKaMixService) {
		this.clusterRepository = clusterRepository;
		this.clusterMapper = clusterMapper;
		this.zooKaMixService = zooKaMixService;
	}

	@Override
	@Transactional
	public ClusterResponseDto create(ClusterCreateDto request) {
		// Map entity
		Cluster entity = clusterMapper.map(request);
		entity = clusterRepository.save(entity);

		// Get version if needed
		entity = setKafkaVersion(entity);

		// return data
		return clusterMapper.map(entity);
	}

	/**
	 * Set kafka version.
	 * 
	 * @param entity the cluster entity
	 * @return the cluster entity
	 */
	private Cluster setKafkaVersion(Cluster entity) {
		if (!StringUtils.hasText(entity.getKafkaVersion())) {
			ZooKaMixAdmin zooKaMixAdmin = zooKaMixService.getConnection(entity.getId());
			try {
				Optional<String> kafkaVersion = zooKaMixAdmin
						.getKafkaVersion(String.valueOf(zooKaMixAdmin.getBrokerList().get(0)));
				if (kafkaVersion.isPresent()) {
					entity.setKafkaVersion(kafkaVersion.get());
					clusterRepository.save(entity);
				}
			} catch (InterruptedException e) {
				log.error("Cannot find kafka version", e);
			}
		}
		return entity;
	}

	@Override
	@Transactional
	public void update(String id, ClusterUpdateDto request) {
		// Get entity
		Cluster entity = clusterRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find cluster " + id));

		// Map entity
		entity = clusterMapper.map(request, entity);

		// Set kafka version
		entity = setKafkaVersion(entity);

		clusterRepository.save(entity);

		// Drop existing connection
		zooKaMixService.dropConnection(entity.getId());
	}

	@Override
	@Transactional
	public void delete(String id) {
		try {
			clusterRepository.deleteById(id);

			// Drop existing connection
			zooKaMixService.dropConnection(id);
		} catch (EmptyResultDataAccessException erdt) {
			throw new ResourceNotFoundException("Data to delete not found: " + id);
		} catch (DataIntegrityViolationException dive) {
			throw new BadRequestException("Unable to delete resource: " + id);
		}
	}

	@Override
	public ClusterResponseDto get(String id) {
		Cluster cluster = clusterRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find cluster " + id));
		return clusterMapper.map(cluster);
	}

	@Override
	public Page<ClusterResponseDto> list(ClusterSearchDto request, Pageable pageable) {
		BooleanBuilder where = new BooleanBuilder();
		if (StringUtils.hasText(request.getName())) {
			where.and(QCluster.cluster.name.contains(request.getName()));
		}
		if (StringUtils.hasText(request.getKafkaVersion())) {
			where.and(QCluster.cluster.kafkaVersion.eq(request.getKafkaVersion()));
		}
		return clusterRepository.findAll(where, pageable).map(clusterMapper::map);
	}

}
