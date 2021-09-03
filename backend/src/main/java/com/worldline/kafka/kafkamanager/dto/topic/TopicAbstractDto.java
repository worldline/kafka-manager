package com.worldline.kafka.kafkamanager.dto.topic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.worldline.kafka.kafkamanager.dto.setting.SettingDto;

import lombok.Data;

/**
 * Abstract topic dto.
 */
@Data
abstract public class TopicAbstractDto implements Serializable {

	private static final long serialVersionUID = -2659217066998957031L;

	@NotBlank
	private String name;

	@Min(1)
	private Integer nbPartitions;

	private int partition;

	private List<SettingDto> configuration;

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
