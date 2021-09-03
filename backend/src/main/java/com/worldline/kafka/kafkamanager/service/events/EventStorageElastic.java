package com.worldline.kafka.kafkamanager.service.events;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventSearchDto;
import com.worldline.kafka.kafkamanager.model.Event;
import com.worldline.kafka.kafkamanager.repository.EventRepository;

/**
 * Elastic implementation for {@link EventStorage}.
 */
public class EventStorageElastic implements EventStorage {

	private EventRepository eventRepository;

	private ElasticsearchOperations elasticsearchOperations;

	private String elasticPrefix;

	/**
	 * Constructor.
	 * 
	 * @param eventRepository         the event repository
	 * @param elasticsearchOperations the ES operations
	 * @param elasticPrefix           the elastic prefix
	 */
	@Autowired
	public EventStorageElastic(EventRepository eventRepository, ElasticsearchOperations elasticsearchOperations,
			@Value("${elasticsearch.prefix}") String elasticPrefix) {
		this.eventRepository = eventRepository;
		this.elasticsearchOperations = elasticsearchOperations;
		this.elasticPrefix = elasticPrefix;
	}

	@Override
	public void save(Event entity) {
		checkIndex(Event.class);
		eventRepository.save(entity);
	}

	/**
	 * Check elastic index.
	 * 
	 * @param <T>  the index type
	 * @param type the index type
	 */
	private <T> void checkIndex(Class<T> type) {
		IndexOperations indexOps = elasticsearchOperations.indexOps(type);
		if (!indexOps.exists()) {
			indexOps.create();
			indexOps.putMapping(type);
		}
	}

	@Override
	public List<Event> search(EventSearchDto request) {
		// Create request
		Criteria criteria = new Criteria();
		if (StringUtils.hasText(request.getClusterId())) {
			criteria = criteria.and(new Criteria("args." + EventArgs.CLUSTER).is(request.getClusterId()));
		}
		if (CollectionUtils.isNotEmpty(request.getTypes())) {
			criteria = criteria.and(new Criteria("topicName").in(request.getTypes()));
		}
		if (request.getStartDate() != null && request.getEndDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
			criteria = criteria.and(new Criteria("creationDate").between(dateFormat.format(request.getStartDate()),
					dateFormat.format(request.getEndDate())));
		}
		if (request.getLastMinutes() > 0) {
			criteria = criteria.and(new Criteria("creationDate").greaterThan("now-" + request.getLastMinutes() + "m"));
		}
		Query searchQuery = new CriteriaQuery(criteria);

		// Get indexes
		List<String> indexes = new ArrayList<>();
		LocalDateTime startDate = (request.getStartDate() != null) ? request.getStartDate() : LocalDateTime.now();
		LocalDateTime endDate = (request.getEndDate() != null) ? request.getEndDate() : LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		do {
			indexes.add(elasticPrefix + "-events-" + startDate.format(formatter));
			startDate = startDate.plus(1, ChronoUnit.DAYS);
		} while (startDate.isBefore(endDate));

		// Execute request
		SearchHits<Event> data = elasticsearchOperations.search(searchQuery, Event.class,
				IndexCoordinates.of(indexes.toArray(new String[indexes.size()])));

		return data.stream().map(SearchHit::getContent).collect(Collectors.toList());
	}

}
