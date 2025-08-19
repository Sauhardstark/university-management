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

import com.sauhard.university.management.backend.dto.CourseResponse;
import com.sauhard.university.management.backend.dto.TeacherResponse;
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
	public ResponseEntity<List<TeacherResponse>> list() {
		List<TeacherResponse> teachers = teacherService.findAll();
		return teachers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(teachers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TeacherResponse> get(@PathVariable UUID id) {
		return ResponseEntity.ok(teacherService.get(id));
	}

	@PostMapping
	public ResponseEntity<TeacherResponse> create(@RequestBody @Valid Teacher teacher) {
		return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(teacher));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TeacherResponse> update(@PathVariable UUID id, @RequestBody @Valid Teacher teacher) {
		return ResponseEntity.ok(teacherService.update(id, teacher));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		teacherService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{teacherId}/courses")
	public ResponseEntity<List<CourseResponse>> courses(@PathVariable UUID teacherId) {
		List<CourseResponse> courses = courseService.listByTeacher(teacherId);
		return courses.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(courses);
	}
}
