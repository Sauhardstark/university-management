package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.TeacherResponse;
import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {
	private final TeacherRepository repo;

	private static TeacherResponse toDto(Teacher t) {
		return TeacherResponse.builder().id(t.getId()).firstName(t.getFirstName()).lastName(t.getLastName())
				.email(t.getEmail()).build();
	}

	public List<TeacherResponse> findAll() {
		return repo.findAll().stream().map(TeacherService::toDto).toList();
	}

	public TeacherResponse get(UUID id) {
		return repo.findById(id).map(TeacherService::toDto)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
	}

	public TeacherResponse create(Teacher t) {
		t.setId(null);
		return toDto(repo.save(t));
	}

	public TeacherResponse update(UUID id, Teacher teacher) {
		teacher.setId(id);
		return toDto(repo.save(teacher));
	}

	public void delete(UUID id) {
		repo.deleteById(id);
	}
}
