package com.sauhard.university.management.backend.dto;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record TeacherResponse(UUID id, String firstName, String lastName, String email) {
}
