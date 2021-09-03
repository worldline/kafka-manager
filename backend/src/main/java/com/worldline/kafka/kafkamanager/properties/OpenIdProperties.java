package com.worldline.kafka.kafkamanager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "authentication.open-id")
public class OpenIdProperties {

	private String adminRoleMapping;

	private List<String> adminWhiteList;
	
	private List<String> adminUsernameMapping;

	private String url;

	private String clientId;

	private String secret;

}
