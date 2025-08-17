package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {
	private final CourseRepository repo;

	public List<Course> findAll() {
		return repo.findAll();
	}

	public Course get(UUID id) {
		return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
	}

	public Course create(Course c) {
		c.setId(null);
		return repo.save(c);
	}

	public Course update(UUID id, Course c) {
		Course existing = get(id);
		existing.setCode(c.getCode());
		existing.setTitle(c.getTitle());
		return repo.save(existing);
	}

	public void delete(UUID id) {
		if (!repo.existsById(id))
			throw new EntityNotFoundException("Course not found: " + id);
		repo.deleteById(id);
	}
}
