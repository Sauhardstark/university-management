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

import com.sauhard.university.management.backend.entities.Enrollment;
import com.sauhard.university.management.backend.services.EnrollmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/students/{studentId}/enrollments")
public class StudentEnrollmentController {
	private final EnrollmentService service;

	public record EnrollmentResponse(UUID id, UUID studentId, UUID courseId) {
	}

	private static EnrollmentResponse toResp(Enrollment e) {
		return new EnrollmentResponse(e.getId(), e.getStudent().getId(), e.getCourse().getId());
	}

	@PutMapping("/{courseId}")
	public ResponseEntity<?> enroll(@PathVariable UUID studentId, @PathVariable UUID courseId) {
		Enrollment e = service.enroll(studentId, courseId);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResp(e));
	}

	@GetMapping
	public List<EnrollmentResponse> list(@PathVariable UUID studentId) {
		return service.listByStudent(studentId).stream().map(StudentEnrollmentController::toResp).toList();
	}
}
