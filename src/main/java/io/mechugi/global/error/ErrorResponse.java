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
		return new ErrorResponse(
				errorCode.getStatus().value(),
				errorCode.name(),
				errorCode.getMessage(),
				path,
				Instant.now()
		);
	}
}
