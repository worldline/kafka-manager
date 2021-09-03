package com.worldline.kafka.kafkamanager.config;

import java.util.Set;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

public class AutoConfigurationSelector extends AutoConfigurationImportSelector {

	@Override
	protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		// Get variables
		Environment environment = getEnvironment();
		Boolean enableElastic = environment.getProperty("elasticsearch.enable", Boolean.class);
		Boolean enableDatabase = environment.getProperty("database.enable", Boolean.class);

		// Manage exclusions
		Set<String> exclusions = super.getExclusions(metadata, attributes);

		// Manage exclusions ES
		if (!enableElastic) {
			exclusions.add(ElasticsearchDataAutoConfiguration.class.getName());
			exclusions.add(ElasticsearchRepositoriesAutoConfiguration.class.getName());
			exclusions.add(ReactiveElasticsearchRepositoriesAutoConfiguration.class.getName());
		}
		
		// Manage exclusions Database
		if(!enableDatabase) {
			exclusions.add(DataSourceAutoConfiguration.class.getName());
		}
		
		return exclusions;
	}

}
