package com.worldline.kafka.kafkamanager.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.cluster.ClusterBrokerAddressDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterCreateDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterResponseDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterSearchDto;
import com.worldline.kafka.kafkamanager.dto.cluster.ClusterUpdateDto;
import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventType;
import com.worldline.kafka.kafkamanager.service.cluster.ClusterService;
import com.worldline.kafka.kafkamanager.service.events.ActivityEvent;
import com.worldline.kafka.kafkamanager.service.events.ActivityEventArg;
import com.worldline.kafka.kafkamanager.service.zookamix.ZooKaMixService;

/**
 * Cluster controller.
 */
@RestController
@RequestMapping("/clusters")
public class ClusterController {

	private ClusterService clusterService;

	private ZooKaMixService zooKaMixService;

	/**
	 * Constructor.
	 * 
	 * @param clusterService  the cluster service
	 * @param zooKaMixService the zookeeper kafka jmx service
	 */
	@Autowired
	public ClusterController(ClusterService clusterService, ZooKaMixService zooKaMixService) {
		this.clusterService = clusterService;
		this.zooKaMixService = zooKaMixService;
	}

	@GetMapping
	public ResponseEntity<Page<ClusterResponseDto>> list(@Valid ClusterSearchDto request, Pageable pageable) {
		return ResponseEntity.ok(clusterService.list(request, pageable));
	}

	@Secured("ADMIN")
	@PostMapping
	@ActivityEvent(value = EventType.CLUSTER_ADD, args = {
			@ActivityEventArg(key = EventArgs.CLUSTER, value = "#request.name") })
	public ResponseEntity<ClusterResponseDto> create(@RequestBody @Valid ClusterCreateDto request) {
		// Check brokers before create
		zooKaMixService.checkKafka(request.getBrokerAddrs().stream().map(ClusterBrokerAddressDto::getKafkaAddress)
				.collect(Collectors.toList()));
		// Create cluster
		return ResponseEntity.status(HttpStatus.CREATED).body(clusterService.create(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClusterResponseDto> get(@PathVariable("id") String id) {
		return ResponseEntity.ok(clusterService.get(id));
	}

	@Secured("ADMIN")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CLUSTER_DELETE, args = {
			@ActivityEventArg(key = EventArgs.CLUSTER, value = "#id") })
	public void delete(@PathVariable("id") String id) {
		clusterService.delete(id);
	}

	@Secured("ADMIN")
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ActivityEvent(value = EventType.CLUSTER_EDIT, args = { @ActivityEventArg(key = EventArgs.CLUSTER, value = "#id") })
	public void update(@PathVariable("id") String id, @RequestBody @Valid ClusterUpdateDto request) {
		// Check brokers before update
		if (request.getBrokerAddrs() != null) {
			zooKaMixService.checkKafka(request.getBrokerAddrs().get().stream()
					.map(ClusterBrokerAddressDto::getKafkaAddress).collect(Collectors.toList()));
		}
		// Update cluster
		clusterService.update(id, request);
	}

}