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

import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
	private final CourseService service;

	public CourseController(CourseService service) {
		this.service = service;
	}

	@GetMapping
	public List<Course> list() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Course get(@PathVariable UUID id) {
		return service.get(id);
	}

	@PostMapping
	public ResponseEntity<Course> create(@RequestBody @Valid Course c) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
	}

	@PutMapping("/{id}")
	public Course update(@PathVariable UUID id, @RequestBody @Valid Course c) {
		return service.update(id, c);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public List<Course> search(@RequestParam("name") String name) {
		return service.searchByName(name);
	}
	
	@PutMapping("/{courseId}/teacher/{teacherId}")
	public ResponseEntity<Course> assign(@PathVariable UUID courseId, @PathVariable UUID teacherId) {
	    Course updated = service.assignTeacher(courseId, teacherId);
	    return ResponseEntity.ok(updated); // 200; idempotent if already assigned
	}

	@DeleteMapping("/{courseId}/teacher")
	public ResponseEntity<Void> unassign(@PathVariable UUID courseId) {
	    boolean changed = service.unassignTeacher(courseId);
	    return changed ? ResponseEntity.noContent().build() : ResponseEntity.noContent().build();
	}
}
