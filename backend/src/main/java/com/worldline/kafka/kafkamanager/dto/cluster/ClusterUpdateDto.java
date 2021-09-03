package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Cluster update dto.
 */
@Data
public class ClusterUpdateDto implements Serializable {

	private static final long serialVersionUID = -4823001433806696761L;

	private Optional<@NotBlank String> name;

	private Optional<@NotBlank String> zkAddr;

	private Optional<@Size(min = 1) List<@Valid @NotNull ClusterBrokerAddressDto>> brokerAddrs;

	private Optional<String> kafkaVersion;

	private Optional<String> comment;

	private Optional<String> kafkaConnectAddr;

}
