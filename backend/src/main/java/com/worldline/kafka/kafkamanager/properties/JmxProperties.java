package com.worldline.kafka.kafkamanager.properties;

import java.io.Serializable;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * JMX Properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jmx")
public class JmxProperties implements Serializable {

	private static final long serialVersionUID = 3366334142925958811L;

	private Map<String, JmxBeanProperties> beans;

	public JmxBeanProperties getType(JmxType type) {
		return beans.get(type.getType());
	}

}
