package com.worldline.kafka.kafkamanager.dto.globalsettings;

import java.io.Serializable;

import lombok.Data;

/**
 * Global settings response dto.
 */
@Data
public class GlobalSettingsResponseDto implements Serializable {

	private static final long serialVersionUID = 4721182864295614131L;

	private boolean enableDatabase;

	private boolean enableElastic;

	private boolean enableMetrics;

	private boolean enableMonitoring;

	private boolean enableKafkaConnect;

	private boolean enableMessageViewer; 

}
