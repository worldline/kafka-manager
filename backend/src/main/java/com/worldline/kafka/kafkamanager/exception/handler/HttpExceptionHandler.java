package com.worldline.kafka.kafkamanager.exception.handler;

import com.worldline.kafka.kafkamanager.exception.OpenIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class HttpExceptionHandler {

	@ExceptionHandler(OpenIdException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleOpenIdException(OpenIdException exception) {
		return this.buildResponse(exception, HttpStatus.UNAUTHORIZED);
	}

	private ResponseEntity<ErrorResponse> buildResponse(RuntimeException exception, HttpStatus httpStatus) {
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return new ResponseEntity<>(errorResponse, httpStatus);
	}

}
