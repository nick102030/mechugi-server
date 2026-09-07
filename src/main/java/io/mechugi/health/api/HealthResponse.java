package io.mechugi.health.api;

import java.time.Instant;

public record HealthResponse(String status, String service, Instant timestamp) {
}
