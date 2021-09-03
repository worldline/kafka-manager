package com.worldline.kafka.kafkamanager.dto.producteur;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Producer message dto.
 */
@Data
public class ProducerMessageDto implements Serializable {

	private static final long serialVersionUID = 5862420225175131361L;

	@NotBlank
	private String message;

	private Map<String, String> headers;

	private int partition = 0;

	private String key;

}
