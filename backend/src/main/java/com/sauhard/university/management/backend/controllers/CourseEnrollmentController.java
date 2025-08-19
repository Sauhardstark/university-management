package com.sauhard.university.management.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sauhard.university.management.backend.dto.EnrollmentResponse;
import com.sauhard.university.management.backend.services.EnrollmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/{courseId}/enrollments")
public class CourseEnrollmentController {
	private final EnrollmentService service;

	@GetMapping
	public ResponseEntity<List<EnrollmentResponse>> list(@PathVariable UUID courseId) {
		List<EnrollmentResponse> enrollmentList = service.listByCourse(courseId);
		return enrollmentList.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.ok(enrollmentList);
	}
}
