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

import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.services.TeacherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
	private final TeacherService service;

	public TeacherController(TeacherService service) {
		this.service = service;
	}

	@GetMapping
	public List<Teacher> list() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Teacher get(@PathVariable UUID id) {
		return service.get(id);
	}

	@PostMapping
	public ResponseEntity<Teacher> create(@RequestBody @Valid Teacher t) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(t));
	}

	@PutMapping("/{id}")
	public Teacher update(@PathVariable UUID id, @RequestBody @Valid Teacher t) {
		return service.update(id, t);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
