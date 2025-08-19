package com.sauhard.university.management.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sauhard.university.management.backend.dto.EnrollmentResponse;
import com.sauhard.university.management.backend.services.EnrollmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/students/{studentId}/enrollments")
public class StudentEnrollmentController {

	private final EnrollmentService service;

	@PutMapping("/{courseId}")
	public ResponseEntity<EnrollmentResponse> enroll(@PathVariable UUID studentId, @PathVariable UUID courseId) {
		EnrollmentResponse resp = service.enroll(studentId, courseId);
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}

	@GetMapping
	public ResponseEntity<List<EnrollmentResponse>> list(@PathVariable UUID studentId) {
		List<EnrollmentResponse> enrollments = service.listByStudent(studentId);
		return enrollments.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.ok(enrollments);
	}
}
