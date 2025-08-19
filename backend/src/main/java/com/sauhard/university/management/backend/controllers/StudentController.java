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

import com.sauhard.university.management.backend.dto.StudentResponse;
import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.services.StudentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentController {
	private final StudentService service;

	@GetMapping
	public ResponseEntity<List<StudentResponse>> list() {
		List<StudentResponse> students = service.findAll();
		return students.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(students);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StudentResponse> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PostMapping
	public ResponseEntity<StudentResponse> create(@RequestBody @Valid Student student) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(student));
	}

	@PutMapping("/{id}")
	public ResponseEntity<StudentResponse> update(@PathVariable UUID id, @RequestBody @Valid Student student) {
		return ResponseEntity.ok(service.update(id, student));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
