package com.sauhard.university.management.backend.dto;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record StudentResponse(UUID id, String firstName, String lastName, String email) {
}
