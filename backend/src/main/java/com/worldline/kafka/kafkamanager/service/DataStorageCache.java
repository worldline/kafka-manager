package com.worldline.kafka.kafkamanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.worldline.kafka.kafkamanager.model.ElasticData;

/**
 * Data storage cache.
 */
public abstract class DataStorageCache<T extends ElasticData> {

	protected final Map<String, List<T>> storage = new HashMap<>();

	protected final int cleanTime;

	/**
	 * Constructor.
	 * 
	 * @param cleanTime the clean time
	 */
	@Autowired
	public DataStorageCache(int cleanTime) {
		this.cleanTime = cleanTime;
	}

	/**
	 * Clean storage.
	 */
	protected void clean() {
		LocalDateTime specificDate = LocalDateTime.now().minusMinutes(cleanTime);

		storage.entrySet().stream().forEach(e -> {
			List<T> metrics = e.getValue();
			storage.put(e.getKey(), metrics.stream().filter(metric -> metric.getCreationDate().isAfter(specificDate))
					.collect(Collectors.toList()));
		});
	}

	/**
	 * Save entity list.
	 * 
	 * @param entities entity list
	 */
	public void save(List<T> entities) {
		if (CollectionUtils.isNotEmpty(entities)) {
			entities.stream().forEach(this::save);
		}
	}

	/**
	 * Save entity.
	 * 
	 * @param entity the entity to save
	 */
	public void save(T entity) {
		final String key = entity.getKey();
		if (!storage.containsKey(key)) {
			storage.put(key, new ArrayList<>());
		}
		storage.get(key).add(entity);
	}

	protected List<T> get(String key) {
		if (!storage.containsKey(key)) {
			return new ArrayList<>();
		}
		return storage.get(key);
	}

}
