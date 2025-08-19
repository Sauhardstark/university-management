package com.sauhard.university.management.backend.dto;

import java.util.UUID;

public record LogEvent(String action, String entityType, UUID entityId, String status, Object oldData,
		Object newData, Object metadata) {
}
