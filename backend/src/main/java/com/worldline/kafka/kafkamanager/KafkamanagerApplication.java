package com.worldline.kafka.kafkamanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.worldline.kafka.kafkamanager.config.AutoConfigurationSelector;

@SpringBootApplication
@Import(AutoConfigurationSelector.class)
@EnableAsync
public class KafkamanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkamanagerApplication.class, args);
	}

}
