package com.worldline.kafka.kafkamanager.model.converter;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple list converter.
 */
@Slf4j
public abstract class ListConverter<T extends Serializable> implements AttributeConverter<List<T>, String> {

	private ObjectMapper mapper = new ObjectMapper();

	private Class<T> type;

	public ListConverter() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public String convertToDatabaseColumn(List<T> stringList) {
		try {
			if (stringList != null) {
				return mapper.writeValueAsString(stringList);
			}
		} catch (JsonProcessingException e) {
			log.error("Cannot serialize", e);
		}
		return null;
	}

	@Override
	public List<T> convertToEntityAttribute(String string) {
		try {
			if (StringUtils.hasText(string)) {
				JavaType typeRef = mapper.getTypeFactory().constructParametricType(List.class, type);
				return mapper.readValue(string, typeRef);
			}
		} catch (JsonProcessingException e) {
			log.error("Cannot deserialize database attribute", e);
		}
		return Collections.emptyList();
	}
}
