package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Abstract cluster dto.
 */
@Data
abstract public class ClusterAbstractDto implements Serializable {

	private static final long serialVersionUID = -3855146267179662627L;

	@NotBlank
	private String name;

	@NotBlank
	private String zkAddr;

	@NotNull
	@Size(min = 1)
	private List<@Valid @NotNull ClusterBrokerAddressDto> brokerAddrs;

	private String kafkaVersion;

	private String comment;

	private String kafkaConnectAddr;

}
