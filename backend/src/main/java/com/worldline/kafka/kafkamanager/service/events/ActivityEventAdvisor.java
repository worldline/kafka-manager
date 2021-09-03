package com.worldline.kafka.kafkamanager.service.events;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Activity event advisor.
 */
@Component
public class ActivityEventAdvisor extends AbstractPointcutAdvisor {

	private static final long serialVersionUID = 1L;

	private transient ActivityEventInterceptor interceptor;

	@Autowired
	public ActivityEventAdvisor(ActivityEventInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	private final transient StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return method.isAnnotationPresent(ActivityEvent.class);
		}
	};

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return interceptor;
	}

}
