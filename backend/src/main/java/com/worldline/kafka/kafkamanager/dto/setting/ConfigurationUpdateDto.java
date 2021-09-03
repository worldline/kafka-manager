package com.worldline.kafka.kafkamanager.dto.setting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Configuration update dto.
 */
@Data
public class ConfigurationUpdateDto implements Serializable {

	private static final long serialVersionUID = 3001338262149797362L;

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
