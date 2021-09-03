package com.worldline.kafka.kafkamanager.service.events;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;

/**
 * Activity event argument.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
@Documented
public @interface ActivityEventArg {

	EventArgs key();

	String value();

}
