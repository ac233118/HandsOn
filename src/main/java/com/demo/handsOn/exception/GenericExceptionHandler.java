package com.demo.handsOn.exception;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<Object> handleResourseException(Exception ex, WebRequest request) throws Exception {
		
		ExceptionBean bean = new ExceptionBean(new Date(), ex.getMessage());
		
		return new ResponseEntity<>(bean, HttpStatus.NOT_FOUND);
	}
	
	
}
