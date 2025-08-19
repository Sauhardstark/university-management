package com.sauhard.university.management.backend.dto;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record EnrollmentResponse(UUID id, UUID studentId, UUID courseId, Integer grade) {
}
