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
import org.springframework.web.bind.annotation.RestController;

import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.services.CourseService;
import com.sauhard.university.management.backend.services.TeacherService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
	private final TeacherService teacherService;

	private final CourseService courseService;

	@GetMapping
	public List<Teacher> list() {
		return teacherService.findAll();
	}

	@GetMapping("/{id}")
	public Teacher get(@PathVariable UUID id) {
		return teacherService.get(id);
	}

	@PostMapping
	public ResponseEntity<Teacher> create(@RequestBody @Valid Teacher t) {
		return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(t));
	}

	@PutMapping("/{id}")
	public Teacher update(@PathVariable UUID id, @RequestBody @Valid Teacher t) {
		return teacherService.update(id, t);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		teacherService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{teacherId}/courses")
	public List<Course> courses(@PathVariable UUID teacherId) {
		return courseService.listByTeacher(teacherId);
	}
}
