package com.adria_alba.gestioMotos.backend.presentation.config;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GestiorCentralizadoExcepciones extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handleGenericException(Exception ex, WebRequest request){
		RespuestaError respuestaError = new RespuestaError(ex.getMessage());
		return handleExceptionInternal(ex, respuestaError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	// ************************************************************************************************************
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// TODO modificar mensaje standard en ingles....
		RespuestaError respuestaError = new RespuestaError(ex.getMessage());
		return handleExceptionInternal(ex, respuestaError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	// ************************************************************************************************************

	@ExceptionHandler(PresentationException.class)
	protected ResponseEntity<?> handlePresentationException(PresentationException ex, WebRequest request){
		RespuestaError respuestaError = new RespuestaError(ex.getMessage());
		return handleExceptionInternal(ex, respuestaError, new HttpHeaders(), ex.getHttpStatus(), request);
	}
	
	// ************************************************************************************************************

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Object valor = ex.getValue();
		String tipoEntrante = valor.getClass().getName();
		String tipoRequerido = (ex.getRequiredType()).getName();
		
		RespuestaError respuestaError = new RespuestaError("El parámetro " + valor + " es de tipo " + tipoEntrante + ". No se puede parsear a " + tipoRequerido);
		return handleExceptionInternal(ex, respuestaError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	// ************************************************************************************************************
	
}
