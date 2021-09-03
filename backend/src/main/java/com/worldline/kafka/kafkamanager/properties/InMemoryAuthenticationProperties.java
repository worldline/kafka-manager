package com.worldline.kafka.kafkamanager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "authentication.in-memory")
public class InMemoryAuthenticationProperties {

	private boolean enabled;

	private String userLogin;

	private String userPasswordHash;

	private String adminLogin;

	private String adminPasswordHash;

}
