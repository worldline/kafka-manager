package com.worldline.kafka.kafkamanager.dto.kafka.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaNodeDto implements Serializable {

	private static final long serialVersionUID = 842490970727696682L;

	private String id;

	private String host;

	private int port;

	private String rack;
}
