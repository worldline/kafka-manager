package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cluster create dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClusterCreateDto extends ClusterAbstractDto implements Serializable {

	private static final long serialVersionUID = -7046807455351066400L;

}
