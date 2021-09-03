package com.worldline.kafka.kafkamanager.kafka;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsDetailsDto;
import com.worldline.kafka.kafkamanager.dto.topic.TopicMetricsResponseDto;
import com.worldline.kafka.kafkamanager.exception.JmxException;
import com.worldline.kafka.kafkamanager.properties.JmxBeanProperties;
import com.worldline.kafka.kafkamanager.properties.JmxProperties;
import com.worldline.kafka.kafkamanager.properties.JmxType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * JMX admin.
 */
@Slf4j
@Data
@AllArgsConstructor
public class JmxAdmin implements Closeable {

	private final JmxProperties jmxProperties;

	private final List<String> jmxUrls;

	private JMXConnector jmxConnector;

	/**
	 * Constructor.
	 * 
	 * @param jmxProperties the jmx properties
	 * @param jmxUrls       the jmx urls
	 */
	public JmxAdmin(JmxProperties jmxProperties, List<String> jmxUrls) {
		this.jmxProperties = jmxProperties;
		this.jmxUrls = jmxUrls;
	}

	/**
	 * Get JMX connector.
	 * 
	 * @return the JMX connector
	 */
	public JMXConnector getJmxConnector() {
		if (jmxConnector == null) {
			try {
				JMXServiceURL target = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + jmxUrls.get(0) + "/jmxrmi");
				jmxConnector = JMXConnectorFactory.connect(target);
			} catch (IOException e) {
				log.error("Cannot connect to JMX", e);
			}
		}
		return jmxConnector;
	}

	@Override
	public void close() throws IOException {
		if (jmxConnector != null) {
			jmxConnector.close();
		}
	}

	/**
	 * Get JMX attributes.
	 * 
	 * @param <T>           the mapping type
	 * @param type          the JMX type
	 * @param classType     the mapping type
	 * @param attributeName the attribute name
	 * @param params        the bean parameters
	 * @return the JMX attributes
	 */
	public <T> Map<String, T> getJmxValues(JmxType type, Class<T> classType, String attributeName, String... params) {
		// Check jmx connection
		// Get jmx data
		JmxBeanProperties jmxBean = getJmxProperties(type);

		JMXConnector jmxConnector = getJmxConnector();
		if (jmxConnector == null) {
			return null;
		}
		try {
			MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
			List<String> attributes = jmxBean.getAttributes();
			if (StringUtils.hasText(attributeName)) {
				attributes = Arrays.asList(attributeName);
			}
			AttributeList attributeList = mBeanServerConnection.getAttributes(
					new ObjectName(String.format(jmxBean.getBean(), params)), attributes.toArray(new String[0]));
			return attributeList.asList().stream().collect(Collectors.toMap(Attribute::getName, a -> (T) a.getValue()));
		} catch (Throwable e) {
			log.error("Cannot find bean attribute: {}", e.getMessage());
		}

		return null;
	}

	/**
	 * Get kafka version.
	 * 
	 * @param brokerId the broker ID
	 * @return the kafka version
	 */
	public Optional<String> getKafkaVersion(String brokerId) {
		Optional<String> kafkaVersion = getKafkaVersion(brokerId, "Version");
		if (!kafkaVersion.isPresent()) {
			// Attribute name is not the same in last versions
			return getKafkaVersion(brokerId, "version");
		}
		return kafkaVersion;
	}

	/**
	 * Get kafka version.
	 * 
	 * @param brokerId         the broker ID
	 * @param attributeVersion the attribute version
	 * @return the kafka version
	 */
	private Optional<String> getKafkaVersion(String brokerId, String attributeVersion) {
		Map<String, String> jmxValues = getJmxValues(JmxType.VERSION, String.class, attributeVersion, brokerId);
		if (jmxValues != null && jmxValues.containsKey(attributeVersion)) {
			return Optional.of(jmxValues.get(attributeVersion));
		}
		return Optional.empty();
	}

	/**
	 * Get metrics topic.
	 * 
	 * @param topicName the topic name
	 * @return the metrics related to the topic
	 */
	public TopicMetricsResponseDto getMetricsTopic(String topicName) {
		TopicMetricsResponseDto result = new TopicMetricsResponseDto();
		Map<JmxType, TopicMetricsDetailsDto> data = new HashMap<>();
		JmxType.getMetricsTypes().stream().forEach(type -> {
			Map<String, Double> jmxValues = getJmxValues(type, Double.class, null, topicName);
			if (jmxValues != null) {
				data.put(type, new TopicMetricsDetailsDto(jmxValues.get("MeanRate"), jmxValues.get("OneMinuteRate"),
						jmxValues.get("FiveMinuteRate"), jmxValues.get("FifteenMinuteRate")));
			}
		});
		result.setBytesInPerSeconds(data.get(JmxType.METRICS_TOPIC_BYTES_IN_PER_SECONDS));
		result.setBytesOutPerSeconds(data.get(JmxType.METRICS_TOPIC_BYTES_OUT_PER_SECONDS));
		result.setBytesRejectedPerSeconds(data.get(JmxType.METRICS_TOPIC_BYTES_REJECTED_PER_SECONDS));
		result.setMessagesPerSeconds(data.get(JmxType.METRICS_TOPIC_MESSAGES_PER_SECONDS));
		result.setFailedFetchPerSeconds(data.get(JmxType.METRICS_TOPIC_FAILED_FETCH_PER_SECONDS));
		result.setFailedProducePerSeconds(data.get(JmxType.METRICS_TOPIC_FAILED_PRODUCE_PER_SECONDS));
		return result;
	}

	/**
	 * Get JMX property.
	 * 
	 * @param type the JMX type
	 * @return the JMX property
	 */
	private JmxBeanProperties getJmxProperties(JmxType type) {
		JmxBeanProperties jmxBean = jmxProperties.getType(type);
		if (jmxBean == null) {
			throw new JmxException("Cannot find jmx property for " + type);
		}
		return jmxBean;
	}

}
