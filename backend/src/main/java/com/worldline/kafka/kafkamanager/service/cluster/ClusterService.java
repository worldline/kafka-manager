package com.worldline.kafka.kafkamanager.service.cluster;

import com.worldline.kafka.kafkamanager.dto.cluster.ClusterCreateDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterResponseDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterSearchDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface of Cluster service.
 */
public interface ClusterService {

	/**
	 * Create cluster.
	 * 
	 * @param request the create request
	 * @return the cluster response
	 */
	public ClusterResponseDto create(ClusterCreateDto request);

	/**
	 * Update cluster.
	 * 
	 * @param id      the cluster ID
	 * @param request the update request
	 */
	public void update(String id, ClusterUpdateDto request);

	/**
	 * Delete cluster.
	 * 
	 * @param id the cluster ID
	 */
	public void delete(String id);

	/**
	 * Get a cluster data.
	 * 
	 * @param id the cluster ID
	 * @return the cluster data
	 */
	public ClusterResponseDto get(String id);

	/**
	 * Get cluster list.
	 * 
	 * @param request  the search request
	 * @param pageable the pageable data
	 * @return the cluster list
	 */
	public Page<ClusterResponseDto> list(ClusterSearchDto request, Pageable pageable);

}
