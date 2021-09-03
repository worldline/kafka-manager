package com.worldline.kafka.kafkamanager.dto.cluster;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cluster response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClusterResponseDto extends ClusterAbstractDto implements Serializable {

	private static final long serialVersionUID = 2802964138121469864L;

	@NotBlank
	private String id;

	@NotNull
	private Date creationDate;

	/**
	 * Get creation date.
	 * 
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate != null ? new Date(creationDate.getTime()) : null;
	}

	/**
	 * Set creation date.
	 * 
	 * @param creationDate the creation Date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
	}
}
