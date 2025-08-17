package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {
	private final TeacherRepository repo;

	public List<Teacher> findAll() {
		return repo.findAll();
	}

	public Teacher get(UUID id) {
		return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
	}

	public Teacher create(Teacher t) {
		t.setId(null);
		return repo.save(t);
	}

	public Teacher update(UUID id, Teacher t) {
		Teacher existing = get(id);
		existing.setFirstName(t.getFirstName());
		existing.setLastName(t.getLastName());
		existing.setEmail(t.getEmail());
		return repo.save(existing);
	}

	public void delete(UUID id) {
		if (!repo.existsById(id))
			throw new EntityNotFoundException("Teacher not found: " + id);
		repo.deleteById(id);
	}
}
