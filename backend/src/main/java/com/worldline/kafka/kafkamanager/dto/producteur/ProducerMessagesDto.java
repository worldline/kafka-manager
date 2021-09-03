package com.worldline.kafka.kafkamanager.dto.producteur;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Producer messages dto.
 */
@Data
public class ProducerMessagesDto implements Serializable {

	private static final long serialVersionUID = -5871249122749107070L;

	@NotNull
	@Size(min = 1)
	private List<@Valid ProducerMessageDto> messages;

}
