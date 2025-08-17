package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	private final StudentRepository repo;

	public List<Student> findAll() {
		return repo.findAll();
	}

	public Student get(UUID id) {
		return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
	}

	public Student create(Student s) {
		s.setId(null);
		return repo.save(s);
	}

	public Student update(UUID id, Student s) {
		Student existing = get(id);
		existing.setFirstName(s.getFirstName());
		existing.setLastName(s.getLastName());
		existing.setEmail(s.getEmail());
		return repo.save(existing);
	}

	public void delete(UUID id) {
		if (!repo.existsById(id))
			throw new EntityNotFoundException("Student not found: " + id);
		repo.deleteById(id);
	}
}