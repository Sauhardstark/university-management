package com.sauhard.university.management.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sauhard.university.management.backend.entities.Enrollment;
import com.sauhard.university.management.backend.services.EnrollmentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/{courseId}/enrollments")
public class CourseEnrollmentController {
	private final EnrollmentService service;

	@GetMapping
	public List<Enrollment> list(@PathVariable UUID courseId) {
		return service.listByCourse(courseId);
	}
}
