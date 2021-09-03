package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Topic creation dto.
 */
@Data
public class TopicReassignDto implements Serializable {

	private static final long serialVersionUID = -8766294359612864220L;

	@NotNull
	@Size(min = 1)
	private List<TopicReassignPartitionDto> partitions;

	@JsonIgnore
	public Map<Integer, List<Integer>> getPartitionsMap() {
		if (CollectionUtils.isNotEmpty(partitions)) {
			return partitions.stream().collect(Collectors.toMap(TopicReassignPartitionDto::getPartitionId,
					TopicReassignPartitionDto::getReplicas));
		}
		return new HashMap<>();
	}

}
