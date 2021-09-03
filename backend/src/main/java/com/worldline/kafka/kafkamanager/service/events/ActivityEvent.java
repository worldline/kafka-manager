package com.worldline.kafka.kafkamanager.service.events;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.worldline.kafka.kafkamanager.dto.event.EventType;

/**
 * Activity event.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ActivityEvent {

	EventType value();

	ActivityEventArg[] args() default {};

	int httpStatus() default 0;

	String response() default "";

	String clusterId() default "#clusterId";

	boolean onlyKo() default false;

}
