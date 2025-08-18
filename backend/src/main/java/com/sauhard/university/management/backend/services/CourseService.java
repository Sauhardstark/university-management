package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.repository.CourseRepository;
import com.sauhard.university.management.backend.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {
	private final CourseRepository courseRepo;

	private final TeacherRepository teacherRepo;

	public List<Course> findAll() {
		return courseRepo.findAll();
	}

	public Course get(UUID id) {
		return courseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
	}

	public Course create(Course c) {
		c.setId(null);
		return courseRepo.save(c);
	}

	public Course update(UUID id, Course course) {
		course.setId(id);
		return courseRepo.save(course);
	}

	public void delete(UUID id) {
		courseRepo.deleteById(id);
	}

	public Course assignTeacher(UUID courseId, UUID teacherId) {
		Course c = get(courseId);
		Teacher t = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + teacherId));
		if (c.getTeacher() != null && teacherId.equals(c.getTeacher().getId()))
			return c;
		c.setTeacher(t);
		return courseRepo.save(c);
	}

	public boolean unassignTeacher(UUID courseId) {
		Course c = get(courseId);
		if (c.getTeacher() == null)
			return false;
		c.setTeacher(null);
		courseRepo.save(c);
		return true;
	}

	public List<Course> searchByName(String namePart) {
		if (namePart == null || namePart.isBlank()) {
			return List.of();
		}
		return courseRepo.findByNameContainingIgnoreCase(namePart.trim());
	}

	public List<Course> listByTeacher(UUID teacherId) {
		if (!teacherRepo.existsById(teacherId))
			throw new EntityNotFoundException("Teacher not found: " + teacherId);
		return courseRepo.findByTeacher_Id(teacherId);
	}
}
