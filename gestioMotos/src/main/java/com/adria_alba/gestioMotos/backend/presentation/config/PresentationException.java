package com.adria_alba.gestioMotos.backend.presentation.config;

import org.springframework.http.HttpStatus;

public class PresentationException  extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;

	public PresentationException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public PresentationException(String message, int statusCode) {
		super(message);
		this.httpStatus = HttpStatus.valueOf(statusCode);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
