package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.LogEvent;
import com.sauhard.university.management.backend.dto.StudentResponse;
import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	private final StudentRepository repo;
	private final ApplicationEventPublisher events;

	private static StudentResponse toDto(Student s) {
		return StudentResponse.builder().id(s.getId()).firstName(s.getFirstName()).lastName(s.getLastName())
				.email(s.getEmail()).build();
	}

	private void log(String action, UUID entityId, String status, Object oldData, Object newData, Object metadata) {
		events.publishEvent(new LogEvent(action, "STUDENT", entityId, status, oldData, newData, metadata));
	}

	public List<StudentResponse> findAll() {
		var out = repo.findAll().stream().map(StudentService::toDto).toList();
		log("STUDENT_LIST", null, "SUCCESS", null, null, Map.of("count", out.size()));
		return out;
	}

	public StudentResponse get(UUID id) {
		var s = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
		var dto = toDto(s);
		log("STUDENT_GET", id, "SUCCESS", null, null, null);
		return dto;
	}

	public StudentResponse create(Student s) {
		s.setId(null);
		var saved = repo.save(s);
		var dto = toDto(saved);
		log("STUDENT_CREATE", saved.getId(), "SUCCESS", null, saved, null);
		return dto;
	}

	public StudentResponse update(UUID id, Student student) {
		var before = repo.findById(id).orElse(null);
		student.setId(id);
		var saved = repo.save(student);
		var dto = toDto(saved);
		log("STUDENT_UPDATE", id, "SUCCESS", before, saved, null);
		return dto;
	}

	public void delete(UUID id) {
		var before = repo.findById(id).orElse(null);
		repo.deleteById(id);
		log("STUDENT_DELETE", id, "SUCCESS", before, null, null);
	}
}
