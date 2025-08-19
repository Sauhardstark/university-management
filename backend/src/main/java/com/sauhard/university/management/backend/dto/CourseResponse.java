package com.sauhard.university.management.backend.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record CourseResponse(UUID id, String code, String name, LocalDate startDate, LocalDate endDate,
		UUID teacherId) {
}
