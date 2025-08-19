package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.StudentResponse;
import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	private final StudentRepository repo;

	private static StudentResponse toDto(Student s) {
		return StudentResponse.builder().id(s.getId()).firstName(s.getFirstName()).lastName(s.getLastName())
				.email(s.getEmail()).build();
	}

	public List<StudentResponse> findAll() {
		return repo.findAll().stream().map(StudentService::toDto).toList();
	}

	public StudentResponse get(UUID id) {
		return repo.findById(id).map(StudentService::toDto)
				.orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
	}

	public StudentResponse create(Student s) {
		s.setId(null);
		return toDto(repo.save(s));
	}

	public StudentResponse update(UUID id, Student student) {
		student.setId(id);
		return toDto(repo.save(student));
	}

	public void delete(UUID id) {
		repo.deleteById(id);
	}
}
