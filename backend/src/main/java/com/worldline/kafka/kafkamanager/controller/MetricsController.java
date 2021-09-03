package com.worldline.kafka.kafkamanager.controller;

import com.worldline.kafka.kafkamanager.dto.metrics.LagMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.LastMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.metrics.MetricSearchDto;
import com.worldline.kafka.kafkamanager.dto.metrics.NbServerMetricResponseDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsResponseDto;
import com.worldline.kafka.kafkamanager.service.MetricsService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Metrics controller.
 */
@RestController
@RequestMapping("/clusters/{clusterId}/metrics")
@ConditionalOnProperty(value = "metrics.enable", matchIfMissing = false)
public class MetricsController {

	private MetricsService metricsService;

	/**
	 * Controller.
	 * 
	 * @param metricsService the metrics service
	 */
	@Autowired
	public MetricsController(MetricsService metricsService) {
		this.metricsService = metricsService;
	}

	@GetMapping
	public ResponseEntity<Page<MetricResponseDto>> list(@PathVariable("clusterId") String clusterId, @Valid MetricSearchDto request, Pageable pageable) {
		return ResponseEntity.ok(metricsService.list(clusterId, request, pageable));
	}

	@GetMapping("/lag")
	public ResponseEntity<LagMetricResponseDto> getLag(@PathVariable("clusterId") String clusterId, @Valid MetricSearchDto request) {
		if(request.getGroupIds().size() != 1){
			return new ResponseEntity("Only one groupdId must be define", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(metricsService.getLag(clusterId, request));
	}

	@GetMapping("/last")
	public ResponseEntity<LastMetricResponseDto> last(@PathVariable("clusterId") String clusterId, @Valid MetricSearchDto request) {
		LastMetricResponseDto metricDto = metricsService.getLastMetricOffset(clusterId, request);

		// Check if metric exist
		return metricDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(metricDto);
	}

	@GetMapping("/group-consumer/servers/count")
	public ResponseEntity<NbServerMetricResponseDto> getNbServers(@PathVariable("clusterId") String clusterId, @Valid MetricSearchDto request) {
		if(request.getGroupIds().size() != 1){
			return new ResponseEntity("Only one groupdId must be define", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(metricsService.getNbServers(clusterId, request));
	}
		
	@GetMapping("/topics/{topicName}")
	public ResponseEntity<TopicMetricsResponseDto> getMetrics(@PathVariable("clusterId") String clusterId, @PathVariable("topicName") String topicName) {
		// Get metrics
		TopicMetricsResponseDto topicMetrics = metricsService.getTopicMetrics(clusterId, topicName);

		// Check if metrics exist
		return topicMetrics.getMessagesPerSeconds() == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(topicMetrics);
	}

}
