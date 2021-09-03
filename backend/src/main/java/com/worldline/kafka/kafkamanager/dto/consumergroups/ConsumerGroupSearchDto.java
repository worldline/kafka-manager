package com.worldline.kafka.kafkamanager.dto.consumergroups;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsumerGroupSearchDto implements Serializable {

	private static final long serialVersionUID = -66998957031L;

	@NotBlank
	private String clusterId;
}
