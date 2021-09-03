package com.worldline.kafka.kafkamanager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "authentication.jwt")
public class JwtProperties {

	private Long expiration;

	private String secret;

}
