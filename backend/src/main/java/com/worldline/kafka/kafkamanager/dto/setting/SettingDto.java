package com.worldline.kafka.kafkamanager.dto.setting;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Setting dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingDto implements Serializable {

	private static final long serialVersionUID = 5807965993960603623L;

	@NotBlank
	private String key;

	private String value;

}
