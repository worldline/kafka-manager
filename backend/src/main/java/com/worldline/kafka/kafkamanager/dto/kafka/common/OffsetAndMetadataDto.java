package com.worldline.kafka.kafkamanager.dto.kafka.common;

import java.io.Serializable;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OffsetAndMetadataDto implements Serializable {

	private static final long serialVersionUID = 4069375307226600698L;

	private long offset;

	private String metadata;

	private Optional<Integer> leaderEpoch;
}
