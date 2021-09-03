package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Nb server metric response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NbServerMetricResponseDto extends AbstractMetricResponseDto implements Serializable {

	private static final long serialVersionUID = 6625645417992038950L;

	public NbServerMetricResponseDto(LocalDateTime date, String clusterId, List<String> groupIds, long nbServer) {
		super(date, clusterId);
		this.groupIds = groupIds;
		this.nbServer = nbServer;
	}

	private List<String> groupIds;
	private long nbServer;
}