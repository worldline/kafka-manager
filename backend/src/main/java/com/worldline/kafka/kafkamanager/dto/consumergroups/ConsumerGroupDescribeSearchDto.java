package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Consuper group describe serach dto.
 */
@Data
public class ConsumerGroupDescribeSearchDto implements Serializable {

	private static final long serialVersionUID = 7648404160489924406L;

	List<String> groupIds;

	List<String> topicNames;

	private boolean offsets;

}
