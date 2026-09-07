package io.mechugi.global.error;

import java.time.Instant;

public record ErrorResponse(
		int status,
		String code,
		String message,
		String path,
		Instant timestamp
) {

	public static ErrorResponse from(ErrorCode errorCode, String path) {
		return of(errorCode, errorCode.getMessage(), path);
	}

	public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
		return new ErrorResponse(
				errorCode.getStatus().value(),
				errorCode.name(),
				message,
				path,
				Instant.now()
		);
	}
}
