package com.worldline.kafka.kafkamanager.service.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventSearchDto;
import com.worldline.kafka.kafkamanager.model.Event;
import com.worldline.kafka.kafkamanager.service.DataStorageCache;

/**
 * Cache implementation for {@link EventStorage}.
 */
public class EventStorageCache extends DataStorageCache<Event> implements EventStorage {

	/**
	 * Constructor.
	 * 
	 * @param cleanTime the clean time
	 */
	public EventStorageCache(@Value("${event.clean.clean-time}") int cleanTime) {
		super(cleanTime);
	}

	@Scheduled(cron = "${event.clean.cron-clean-storage}")
	private void executeClean() {
		clean();
	}

	@Override
	public List<Event> search(EventSearchDto request) {
		return get(Event.EVENT_KEY).stream().filter(event -> searchFilter(event, request)).collect(Collectors.toList());
	}

	/**
	 * Search filter.
	 * 
	 * @param event   the event
	 * @param request the search request
	 * @return {@code true} if the event must be returned
	 */
	private boolean searchFilter(Event event, EventSearchDto request) {
		boolean result = true;
		if (StringUtils.hasText(request.getClusterId())) {
			String clusterId = event.getArg(EventArgs.CLUSTER);
			result &= clusterId != null && request.getClusterId().equals(clusterId);
		}
		if (CollectionUtils.isNotEmpty(request.getTypes())) {
			result &= request.getTypes().contains(event.getType());
		}
		if (request.getStartDate() != null && request.getEndDate() != null) {
			result &= request.getStartDate().isBefore(event.getCreationDate())
					&& request.getEndDate().isAfter(event.getCreationDate());
		}
		if (request.getLastMinutes() > 0) {
			result &= LocalDateTime.now().minusMinutes(request.getLastMinutes()).isBefore(event.getCreationDate());
		}
		return result;
	}

}
