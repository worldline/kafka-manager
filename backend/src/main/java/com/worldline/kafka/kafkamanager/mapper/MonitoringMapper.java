package com.worldline.kafka.kafkamanager.mapper;

import com.worldline.kafka.kafkamanager.dto.monitoring.MonitoringResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Monitoring mapper.
 */
@Service
public class MonitoringMapper {

	private static final String STATUS_UP = "UP";
	private static final String STATUS_DOWN = "DOWN";
	
	// Hide constructor 
	private MonitoringMapper(){}
	
	/**
	 * Create a MonitoringResponseDto with a condition
	 * @param localDateTime the date
	 * @param condition the condition   
	 * @param comment the comment
	 * @return {@link MonitoringResponseDto}
	 */
	public static MonitoringResponseDto map(LocalDateTime localDateTime, boolean condition, String comment) {
		MonitoringResponseDto data = new MonitoringResponseDto();
		data.setDate(localDateTime);
		data.setStatus(condition ? STATUS_UP : STATUS_DOWN);
		data.setComment(comment);
		return data;
	}

	/**
	 * Create a MonitoringResponseDto with a condition
	 * @param localDateTime the date
	 * @param condition the condition   
	 * @return {@link MonitoringResponseDto}
	 */
	public static MonitoringResponseDto map(LocalDateTime localDateTime, boolean condition) {
		return MonitoringMapper.map(localDateTime, condition, null);
	}
}
