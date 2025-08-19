package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.CourseResponse;
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

	private static CourseResponse toDto(Course c) {
		UUID tid = (c.getTeacher() != null) ? c.getTeacher().getId() : null;
		return CourseResponse.builder().id(c.getId()).code(c.getCode()).name(c.getName()).startDate(c.getStartDate())
				.endDate(c.getEndDate()).teacherId(tid).build();
	}

	public List<CourseResponse> findAll() {
		return courseRepo.findAll().stream().map(CourseService::toDto).toList();
	}

	public CourseResponse get(UUID id) {
		return courseRepo.findById(id).map(CourseService::toDto)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
	}

	public CourseResponse create(Course c) {
		c.setId(null);
		return toDto(courseRepo.save(c));
	}

	public CourseResponse update(UUID id, Course course) {
		course.setId(id);
		return toDto(courseRepo.save(course));
	}

	public void delete(UUID id) {
		courseRepo.deleteById(id);
	}

	public CourseResponse assignTeacher(UUID courseId, UUID teacherId) {
		Course c = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
		Teacher t = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + teacherId));
		if (c.getTeacher() != null && teacherId.equals(c.getTeacher().getId()))
			return toDto(c);
		c.setTeacher(t);
		return toDto(courseRepo.save(c));
	}

	public boolean unassignTeacher(UUID courseId) {
		Course c = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
		if (c.getTeacher() == null)
			return false;
		c.setTeacher(null);
		courseRepo.save(c);
		return true;
	}

	public List<CourseResponse> searchByName(String namePart) {
		if (namePart == null || namePart.isBlank())
			return List.of();
		return courseRepo.findByNameContainingIgnoreCase(namePart.trim()).stream().map(CourseService::toDto).toList();
	}

	public List<CourseResponse> listByTeacher(UUID teacherId) {
		if (!teacherRepo.existsById(teacherId))
			throw new EntityNotFoundException("Teacher not found: " + teacherId);
		return courseRepo.findByTeacher_Id(teacherId).stream().map(CourseService::toDto).toList();
	}
}
