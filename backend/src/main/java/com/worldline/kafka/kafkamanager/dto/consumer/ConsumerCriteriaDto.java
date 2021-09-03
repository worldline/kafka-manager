package com.worldline.kafka.kafkamanager.dto.consumer;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ConsumerCriteriaDto {

	boolean commit;

	@NotBlank
	String consumerGroupId;

	boolean fromBeginning;

	@Min(1)
	int numberOfMessages;

}
