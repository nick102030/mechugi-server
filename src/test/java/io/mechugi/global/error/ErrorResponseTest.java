package io.mechugi.global.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

	@Test
	void createsResponseFromErrorCode() {
		ErrorResponse response = ErrorResponse.from(
				ErrorCode.INVALID_REQUEST,
				"/api/quizzes"
		);

		assertThat(response.status()).isEqualTo(400);
		assertThat(response.code()).isEqualTo("INVALID_REQUEST");
		assertThat(response.message()).isEqualTo("요청 값이 올바르지 않음");
		assertThat(response.path()).isEqualTo("/api/quizzes");
		assertThat(response.timestamp()).isNotNull();
	}
}
