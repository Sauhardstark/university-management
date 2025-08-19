package com.sauhard.university.management.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sauhard.university.management.backend.dto.CourseResponse;
import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.services.CourseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {
	private final CourseService service;

	@GetMapping
	public ResponseEntity<List<CourseResponse>> list() {
		List<CourseResponse> courses = service.findAll();
		return courses.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(courses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CourseResponse> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PostMapping
	public ResponseEntity<CourseResponse> create(@RequestBody @Valid Course course) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(course));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CourseResponse> update(@PathVariable UUID id, @RequestBody @Valid Course course) {
		return ResponseEntity.ok(service.update(id, course));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<CourseResponse>> search(@RequestParam("name") String name) {
		List<CourseResponse> courses = service.searchByName(name);
		return courses.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(courses);
	}

	@PutMapping("/{courseId}/teacher/{teacherId}")
	public ResponseEntity<CourseResponse> assign(@PathVariable UUID courseId, @PathVariable UUID teacherId) {
		CourseResponse updated = service.assignTeacher(courseId, teacherId);
		return ResponseEntity.ok(updated); // 200; idempotent if already assigned
	}

	@DeleteMapping("/{courseId}/teacher")
	public ResponseEntity<Void> unassign(@PathVariable UUID courseId) {
		boolean changed = service.unassignTeacher(courseId);
		return changed ? ResponseEntity.noContent().build() : ResponseEntity.noContent().build();
	}
}
