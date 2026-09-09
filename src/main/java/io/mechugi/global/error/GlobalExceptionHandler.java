package io.mechugi.global.error;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(
			BusinessException exception,
			HttpServletRequest request
	) {
		ErrorCode errorCode = exception.getErrorCode();
		ErrorResponse response = ErrorResponse.from(errorCode, request.getRequestURI());

		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception,
			HttpServletRequest request
	) {
		ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(errorCode.getMessage());

		return badRequest(message, request);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(
			HandlerMethodValidationException exception,
			HttpServletRequest request
	) {
		String message = exception.getAllErrors().stream()
				.map(error -> error.getDefaultMessage())
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(ErrorCode.INVALID_REQUEST.getMessage());

		return badRequest(message, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(
			ConstraintViolationException exception,
			HttpServletRequest request
	) {
		String message = exception.getConstraintViolations().stream()
				.map(violation -> violation.getMessage())
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(ErrorCode.INVALID_REQUEST.getMessage());

		return badRequest(message, request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException exception,
			HttpServletRequest request
	) {
		String message = exception.getName() + " 값의 형식이 올바르지 않음";

		return badRequest(message, request);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException exception,
			HttpServletRequest request
	) {
		String message = exception.getParameterName() + " 파라미터는 필수임";

		return badRequest(message, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException exception,
			HttpServletRequest request
	) {
		return badRequest("요청 본문의 형식이 올바르지 않음", request);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
			NoResourceFoundException exception,
			HttpServletRequest request
	) {
		return errorResponse(ErrorCode.API_NOT_FOUND, request);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception,
			HttpServletRequest request
	) {
		return errorResponse(ErrorCode.METHOD_NOT_ALLOWED, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
			Exception exception,
			HttpServletRequest request
	) {
		log.error("Unhandled exception occurred at {}", request.getRequestURI(), exception);

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse response = ErrorResponse.from(errorCode, request.getRequestURI());

		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	private ResponseEntity<ErrorResponse> badRequest(String message, HttpServletRequest request) {
		ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
		ErrorResponse response = ErrorResponse.of(errorCode, message, request.getRequestURI());

		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	private ResponseEntity<ErrorResponse> errorResponse(
			ErrorCode errorCode,
			HttpServletRequest request
	) {
		ErrorResponse response = ErrorResponse.from(errorCode, request.getRequestURI());

		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}
}
