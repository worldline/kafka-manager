package com.worldline.kafka.kafkamanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.model.converter.BrokerAddressConverter;

import lombok.Data;

/**
 * Cluster model.
 */
@Data
@Entity
public class Cluster {

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private String name;

	private String zkAddr;

	@Convert(converter = BrokerAddressConverter.class)
	private List<BrokerAddress> brokerAddrs;

	private String kafkaVersion;

	private String comment;

	private String kafkaConnectAddr;

	/**
	 * Pre persist action.
	 */
	@PrePersist
	private void prePersist() {
		if (!StringUtils.hasText(id)) {
			id = UUID.randomUUID().toString();
		}
		this.creationDate = new Date();
	}

	/**
	 * Get kafka address.
	 * 
	 * @return kafka address list
	 */
	public List<String> getKafkaAddress() {
		if (CollectionUtils.isNotEmpty(brokerAddrs)) {
			return brokerAddrs.stream().map(BrokerAddress::getKafkaAddress).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	/**
	 * Get JMX address.
	 * 
	 * @return kafka address list
	 */
	public List<String> getJmxAddress() {
		if (CollectionUtils.isNotEmpty(brokerAddrs)) {
			return brokerAddrs.stream().map(BrokerAddress::getJmxAddress).filter(Optional::isPresent).map(Optional::get)
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

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
