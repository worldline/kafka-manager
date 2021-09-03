package com.worldline.kafka.kafkamanager.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.worldline.kafka.kafkamanager.model.Cluster;

/**
 * Repository for {@link Cluster}.
 */
public interface ClusterRepository
		extends PagingAndSortingRepository<Cluster, String>, QuerydslPredicateExecutor<Cluster> {

}
