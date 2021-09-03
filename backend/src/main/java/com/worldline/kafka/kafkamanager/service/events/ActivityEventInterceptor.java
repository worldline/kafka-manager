package com.worldline.kafka.kafkamanager.service.events;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.worldline.kafka.kafkamanager.dto.event.EventArgs;
import com.worldline.kafka.kafkamanager.dto.event.EventStatus;
import com.worldline.kafka.kafkamanager.service.EventService;

/**
 * Activity event interceptor.
 */
@Component
public class ActivityEventInterceptor implements MethodInterceptor {

	@Autowired
	private EventService eventService;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		ActivityEvent activityEvent = methodInvocation.getMethod().getAnnotation(ActivityEvent.class);

		// Continue if there is no invocation
		if (activityEvent == null) {
			return methodInvocation.proceed();
		}

		// Parse SPEL expression
		Map<String, String> arguments = null;
		if (activityEvent.args() != null && activityEvent.args().length > 0) {
			arguments = getArguments(methodInvocation, activityEvent.args(), activityEvent.clusterId());
		}

		// Execute action
		try {
			Object result = methodInvocation.proceed();
			EventStatus eventStatus = getStatusOnResponse(result, activityEvent.httpStatus(), activityEvent.response());
			if (!activityEvent.onlyKo() || EventStatus.KO.equals(eventStatus)) {
				eventService.create(activityEvent.value(), eventStatus, arguments);
			}
			return result;
		} catch (Throwable exception) {
			eventService.create(activityEvent.value(), EventStatus.KO, arguments);
			throw exception;
		}

	}

	/**
	 * Get event status depends on the http status.
	 * 
	 * @param result     the response
	 * @param httpStatus the http status code to compare
	 * @param response   the SPEL response
	 * @return the event status
	 */
	private EventStatus getStatusOnResponse(Object result, int httpStatus, String response) {
		// Check the http status value for response entity
		if (httpStatus > 0 && result != null && result instanceof ResponseEntity) {
			ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
			if (httpStatus != responseEntity.getStatusCodeValue()) {
				return EventStatus.KO;
			}
		}

		// Check SPEL response
		if (StringUtils.hasText(response)) {
			ExpressionParser parser = new SpelExpressionParser();
			StandardEvaluationContext context = new StandardEvaluationContext(result);
			if (!(boolean) parser.parseExpression(response).getValue(context)) {
				return EventStatus.KO;
			}
		}

		return EventStatus.OK;
	}

	/**
	 * Parse SPEL arguments.
	 * 
	 * @param methodInvocation the method invocation
	 * @param args             the SPEL arguments
	 * @return the transformed values
	 */
	private Map<String, String> getArguments(MethodInvocation methodInvocation, ActivityEventArg[] args,
			String clusterId) {
		Map<String, String> arguments = new HashMap<>();
		ExpressionParser parser = new SpelExpressionParser();
		MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(methodInvocation,
				methodInvocation.getMethod(), methodInvocation.getArguments(), new DefaultParameterNameDiscoverer());
		for (int i = 0; i < args.length; i++) {
			ActivityEventArg activityEventArg = args[i];
			Object object = parser.parseExpression(activityEventArg.value()).getValue(context);
			String argValue = null;
			if (Integer.class.equals(object.getClass())) {
				argValue = ((Integer) object).toString();
			} else {
				argValue = (String) object;
			}
			arguments.put(activityEventArg.key().name(), argValue);
		}
		if (!arguments.containsKey(EventArgs.CLUSTER.name())) {
			arguments.put(EventArgs.CLUSTER.name(), (String) parser.parseExpression(clusterId).getValue(context));
		}
		return arguments;
	}

}
