package com.worldline.kafka.kafkamanager.mapper;

import com.worldline.kafka.kafkamanager.dto.kafka.common.OffsetAndMetadataDto;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.springframework.stereotype.Service;

@Service
public class OffsetAndMetadataMapper {

	/**
	 * Map native kafka {@link OffsetAndMetadata} to {@link OffsetAndMetadata}.
	 */
	public OffsetAndMetadataDto mapToDto(OffsetAndMetadata raw) {
		return OffsetAndMetadataDto.builder()
			.offset(raw.offset())
			.metadata(raw.metadata())
			.leaderEpoch(raw.leaderEpoch())
			.build();
	}

	/**
	 * Map {@link OffsetAndMetadata} to native kafka {@link OffsetAndMetadata}.
	 */
	public OffsetAndMetadata mapToNative(OffsetAndMetadataDto dto) {
		return new OffsetAndMetadata(dto.getOffset(), dto.getLeaderEpoch(), dto.getMetadata());
	}

}
