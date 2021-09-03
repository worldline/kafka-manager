package com.worldline.kafka.kafkamanager.dto.record;

import lombok.Data;

@Data
public class RecordToDeleteDto {
	private int partition;
	private long offsetToDeleteUntil;
}
