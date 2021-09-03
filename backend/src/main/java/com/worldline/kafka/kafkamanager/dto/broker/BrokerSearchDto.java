package com.worldline.kafka.kafkamanager.dto.broker;

import java.io.Serializable;

import lombok.Data;

/**
 * Broker search dto.
 */
@Data
public class BrokerSearchDto implements Serializable {

	private static final long serialVersionUID = -4301219790460031634L;

	private boolean full;

}
