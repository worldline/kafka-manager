package com.worldline.kafka.kafkamanager.model;

import java.io.Serializable;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Broker address.
 */
@Data
public class BrokerAddress implements Serializable {

	private static final long serialVersionUID = -1970315791480524614L;

	private String address;

	private int kafkaPort;

	private int jmxPort;

	/**
	 * Get kafka address.
	 * 
	 * @return kafka address
	 */
	@JsonIgnore
	public String getKafkaAddress() {
		return address + ":" + kafkaPort;
	}

	/**
	 * Get JMX address.
	 * 
	 * @return JMX address
	 */
	@JsonIgnore
	public Optional<String> getJmxAddress() {
		if (jmxPort > 0) {
			return Optional.of(address + ":" + jmxPort);
		}
		return Optional.empty();
	}

}
