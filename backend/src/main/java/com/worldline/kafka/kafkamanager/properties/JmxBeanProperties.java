package com.worldline.kafka.kafkamanager.properties;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Jmx bean properties.
 */
@Data
public class JmxBeanProperties implements Serializable {

	private static final long serialVersionUID = 7334031395495572489L;

	private String bean;

	private List<String> attributes;

}
