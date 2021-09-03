package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Cluster broker address.
 */
@Data
public class ClusterBrokerAddressDto implements Serializable {

	private static final long serialVersionUID = -6033583671849726256L;

	@NotBlank
	private String address;

	@Max(65535)
	@Min(0)
	private Integer kafkaPort;

	@Max(65535)
	@Min(0)
	private Integer jmxPort;

	/**
	 * Get kafka address.
	 * 
	 * @return kafka address
	 */
	@JsonIgnore
	public String getKafkaAddress() {
		return address + ":" + kafkaPort;
	}

}
