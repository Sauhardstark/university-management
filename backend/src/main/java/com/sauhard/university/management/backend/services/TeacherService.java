package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.LogEvent;
import com.sauhard.university.management.backend.dto.TeacherResponse;
import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {
	private final TeacherRepository repo;
	private final ApplicationEventPublisher events;

	private static TeacherResponse toDto(Teacher t) {
		return TeacherResponse.builder().id(t.getId()).firstName(t.getFirstName()).lastName(t.getLastName())
				.email(t.getEmail()).build();
	}

	private void log(String action, UUID entityId, String status, Object oldData, Object newData, Object metadata) {
		events.publishEvent(new LogEvent(action, "TEACHER", entityId, status, oldData, newData, metadata));
	}

	public List<TeacherResponse> findAll() {
		var out = repo.findAll().stream().map(TeacherService::toDto).toList();
		log("TEACHER_LIST", null, "SUCCESS", null, null, Map.of("count", out.size()));
		return out;
	}

	public TeacherResponse get(UUID id) {
		var t = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
		var dto = toDto(t);
		log("TEACHER_GET", id, "SUCCESS", null, null, null);
		return dto;
	}

	public TeacherResponse create(Teacher t) {
		t.setId(null);
		var saved = repo.save(t);
		var dto = toDto(saved);
		log("TEACHER_CREATE", saved.getId(), "SUCCESS", null, saved, null);
		return dto;
	}

	public TeacherResponse update(UUID id, Teacher teacher) {
		var before = repo.findById(id).orElse(null);
		teacher.setId(id);
		var saved = repo.save(teacher);
		var dto = toDto(saved);
		log("TEACHER_UPDATE", id, "SUCCESS", before, saved, null);
		return dto;
	}

	public void delete(UUID id) {
		var before = repo.findById(id).orElse(null);
		repo.deleteById(id);
		log("TEACHER_DELETE", id, "SUCCESS", before, null, null);
	}
}
