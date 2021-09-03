package com.worldline.kafka.kafkamanager.dto.metrics;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Metrics search dto.
 */
@Data
public class MetricSearchDto implements Serializable {

	private static final long serialVersionUID = -6579264110129882826L;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;

	@Min(value = 0)
	private int lastMinutes;
	
	private List<String> topicNames = Collections.emptyList();
	private List<String> topicNamesExcluded = Collections.emptyList();
	
	public List<Integer> partitions = Collections.emptyList();
	public List<Integer> partitionsExcluded = Collections.emptyList();
	
	private List<String> groupIds = Collections.emptyList();
	private List<String> groupIdsExcluded = Collections.emptyList();
}
