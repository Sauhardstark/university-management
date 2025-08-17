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
	public List<Student> list() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Student get(@PathVariable UUID id) {
		return service.get(id);
	}

	@PostMapping
	public ResponseEntity<Student> create(@RequestBody @Valid Student s) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(s));
	}

	@PutMapping("/{id}")
	public Student update(@PathVariable UUID id, @RequestBody @Valid Student s) {
		return service.update(id, s);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
