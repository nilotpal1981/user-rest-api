package com.nilotpal.api.userapi.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nilotpal.api.userapi.exceptions.UserNotFoundException;
import com.nilotpal.api.userapi.model.ApiException;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
	protected ResponseEntity<ApiException> handleUserNotFoundException(HttpServletRequest request,
			UserNotFoundException exception) {
		String errorMessage = StringUtils.isEmpty(exception.getMessage()) ? "User Not Found" : exception.getMessage();
		return buildResponseEntity(new ApiException(HttpStatus.NOT_FOUND, errorMessage, exception));
	}

	private ResponseEntity<ApiException> buildResponseEntity(ApiException apiException) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<ApiException>(apiException, requestHeaders, apiException.getStatus());
	}
}
