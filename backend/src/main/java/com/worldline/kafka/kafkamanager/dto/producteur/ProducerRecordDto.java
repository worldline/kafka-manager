package com.worldline.kafka.kafkamanager.dto.producteur;

import java.io.Serializable;

import com.worldline.kafka.kafkamanager.dto.record.RecordDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Producer record dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProducerRecordDto extends RecordDto implements Serializable {

	private static final long serialVersionUID = 8272574079664321250L;

}
