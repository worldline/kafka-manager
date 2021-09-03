package com.worldline.kafka.kafkamanager.dto.broker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.worldline.kafka.kafkamanager.dto.setting.SettingDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Broker response dto.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BrokerResponseDto extends BrokerAbstractDto implements Serializable {

	private static final long serialVersionUID = -4805704692915338414L;

	private int id;

	private BrokerStatus status;

	@NotBlank
	private String host;

	private int port;

	private Integer nbTopics;

	private Integer nbPartitions;

	private Integer nbLeaderPartitions;

	private List<@Valid SettingDto> configuration;

	/**
	 * Get configuration map.
	 * 
	 * @return the configuration map
	 */
	@JsonIgnore
	public Map<String, String> getConfigurationMap() {
		if (CollectionUtils.isNotEmpty(configuration)) {
			return configuration.stream().collect(Collectors.toMap(SettingDto::getKey, SettingDto::getValue));
		}
		return new HashMap<>();
	}

	/**
	 * Set configuration map.
	 * 
	 * @param configuration the configuration map
	 */
	public void setConfigurationMap(Map<String, String> configuration) {
		if (configuration != null && !configuration.isEmpty()) {
			this.configuration = configuration.entrySet().stream().map(e -> new SettingDto(e.getKey(), e.getValue()))
					.collect(Collectors.toList());
		}
	}

}
