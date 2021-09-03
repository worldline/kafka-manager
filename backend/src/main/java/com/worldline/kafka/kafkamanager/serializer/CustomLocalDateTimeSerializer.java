package com.worldline.kafka.kafkamanager.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	private static final long serialVersionUID = 7253063546033066158L;

	protected CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
		super(t);
	}

	protected CustomLocalDateTimeSerializer() {
		this(null);
	}

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeNumber(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}
}