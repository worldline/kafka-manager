package com.worldline.kafka.kafkamanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.kafka.kafkamanager.dto.globalsettings.GlobalSettingsResponseDto;

/**
 * Global settings controller.
 */
@RestController
@RequestMapping("/global-settings")
public class GlobalSettingsController {

	private boolean enableDatabase;

	private boolean enableElastic;

	private boolean enableMetrics;

	private boolean enableMonitoring;

	private boolean enableKafkaConnect;

	@Autowired
	public GlobalSettingsController(@Value("${database.enable}") boolean enableDatabase,
			@Value("${elasticsearch.enable}") boolean enableElastic, @Value("${metrics.enable}") boolean enableMetrics,
			@Value("${monitoring.enable}") boolean enableMonitoring,
			@Value("${kafka-connect.enable}") boolean enableKafkaConnect) {
		this.enableDatabase = enableDatabase;
		this.enableElastic = enableElastic;
		this.enableMetrics = enableMetrics;
		this.enableMonitoring = enableMonitoring;
		this.enableKafkaConnect = enableKafkaConnect;
	}

	@GetMapping
	public ResponseEntity<GlobalSettingsResponseDto> get() {
		GlobalSettingsResponseDto response = new GlobalSettingsResponseDto();
		response.setEnableDatabase(enableDatabase);
		response.setEnableElastic(enableElastic);
		response.setEnableMetrics(enableMetrics);
		response.setEnableMonitoring(enableMonitoring);
		response.setEnableKafkaConnect(enableKafkaConnect);
		return ResponseEntity.ok(response);
	}

}
