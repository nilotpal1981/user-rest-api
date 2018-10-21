package com.nilotpal.api.userapi.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApiException {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;

	private ApiException() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiException(HttpStatus status) {
		this();
		this.status = status;
	}

	public ApiException(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public ApiException(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}
}