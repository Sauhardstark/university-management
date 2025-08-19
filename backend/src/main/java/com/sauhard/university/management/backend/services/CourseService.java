package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.CourseResponse;
import com.sauhard.university.management.backend.dto.LogEvent;
import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.repository.CourseRepository;
import com.sauhard.university.management.backend.repository.TeacherRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {
	private final CourseRepository courseRepo;
	private final TeacherRepository teacherRepo;
	private final ApplicationEventPublisher events;

	private static CourseResponse toDto(Course c) {
		UUID tid = (c.getTeacher() != null) ? c.getTeacher().getId() : null;
		return CourseResponse.builder().id(c.getId()).code(c.getCode()).name(c.getName()).startDate(c.getStartDate())
				.endDate(c.getEndDate()).teacherId(tid).build();
	}

	public List<CourseResponse> findAll() {
		var out = courseRepo.findAll().stream().map(CourseService::toDto).toList();
		log("COURSE_LIST", null, "SUCCESS", null, null, Map.of("count", out.size()));
		return out;
	}

	public CourseResponse get(UUID id) {
		var c = courseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
		var dto = toDto(c);
		log("COURSE_GET", id, "SUCCESS", null, null, null);
		return dto;
	}

	public CourseResponse create(Course c) {
		c.setId(null);
		var saved = courseRepo.save(c);
		var dto = toDto(saved);
		log("COURSE_CREATE", saved.getId(), "SUCCESS", null, saved, null);
		return dto;
	}

	public CourseResponse update(UUID id, Course course) {
		var before = courseRepo.findById(id).orElse(null);
		course.setId(id);
		var saved = courseRepo.save(course);
		var dto = toDto(saved);
		log("COURSE_UPDATE", id, "SUCCESS", before, saved, null);
		return dto;
	}

	public void delete(UUID id) {
		var before = courseRepo.findById(id).orElse(null);
		courseRepo.deleteById(id);
		log("COURSE_DELETE", id, "SUCCESS", before, null, null);
	}

	public CourseResponse assignTeacher(UUID courseId, UUID teacherId) {
		var c = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
		var t = teacherRepo.findById(teacherId)
				.orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + teacherId));
		if (c.getTeacher() != null && teacherId.equals(c.getTeacher().getId())) {
			log("COURSE_ASSIGN_TEACHER", courseId, "SUCCESS", c, c, Map.of("nochange", true));
			return toDto(c);
		}
		var before = c;
		c.setTeacher(t);
		var saved = courseRepo.save(c);
		var dto = toDto(saved);
		log("COURSE_ASSIGN_TEACHER", courseId, "SUCCESS", before, saved, Map.of("teacherId", teacherId));
		return dto;
	}

	public boolean unassignTeacher(UUID courseId) {
		var c = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
		if (c.getTeacher() == null) {
			log("COURSE_UNASSIGN_TEACHER", courseId, "SUCCESS", c, c, Map.of("nochange", true));
			return false;
		}
		var before = c;
		c.setTeacher(null);
		courseRepo.save(c);
		log("COURSE_UNASSIGN_TEACHER", courseId, "SUCCESS", before, c, null);
		return true;
	}

	public List<CourseResponse> searchByName(String namePart) {
		if (namePart == null || namePart.isBlank()) {
			log("COURSE_SEARCH", null, "SUCCESS", null, null, Map.of("q", "", "count", 0));
			return List.of();
		}
		var out = courseRepo.findByNameContainingIgnoreCase(namePart.trim()).stream().map(CourseService::toDto)
				.toList();
		log("COURSE_SEARCH", null, "SUCCESS", null, null, Map.of("q", namePart.trim(), "count", out.size()));
		return out;
	}

	public List<CourseResponse> listByTeacher(UUID teacherId) {
		if (!teacherRepo.existsById(teacherId))
			throw new EntityNotFoundException("Teacher not found: " + teacherId);
		var out = courseRepo.findByTeacher_Id(teacherId).stream().map(CourseService::toDto).toList();
		log("COURSE_LIST_BY_TEACHER", null, "SUCCESS", null, null, Map.of("teacherId", teacherId, "count", out.size()));
		return out;
	}

	private void log(String action, UUID entityId, String status, Object oldData, Object newData, Object metadata) {
		events.publishEvent(new LogEvent(action, "COURSE", entityId, status, oldData, newData, metadata));
	}
}
