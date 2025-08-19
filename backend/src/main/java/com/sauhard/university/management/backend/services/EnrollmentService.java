package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.sauhard.university.management.backend.dto.EnrollmentResponse;
import com.sauhard.university.management.backend.dto.LogEvent;
import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.entities.Enrollment;
import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.repository.CourseRepository;
import com.sauhard.university.management.backend.repository.EnrollmentRepository;
import com.sauhard.university.management.backend.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepo;
	private final StudentRepository studentRepo;
	private final CourseRepository courseRepo;
	private final ApplicationEventPublisher events;

	private static EnrollmentResponse toDto(Enrollment e) {
		return EnrollmentResponse.builder().id(e.getId()).studentId(e.getStudent().getId())
				.courseId(e.getCourse().getId()).grade(e.getGrade()).build();
	}

	private void log(String action, UUID entityId, String status, Object oldData, Object newData, Object metadata) {
		events.publishEvent(new LogEvent(action, "ENROLLMENT", entityId, status, oldData, newData, metadata));
	}

	public EnrollmentResponse enroll(UUID studentId, UUID courseId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));
		Course course = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

		Enrollment existing = enrollmentRepo.findByStudent_IdAndCourse_Id(studentId, courseId);
		if (existing != null) {
			log("ENROLLMENT_CREATE", existing.getId(), "SUCCESS", existing, existing,
					Map.of("studentId", studentId, "courseId", courseId, "idempotent", true));
			return toDto(existing);
		}

		boolean overlaps = enrollmentRepo.existsOverlapWithOtherCourses(studentId, courseId, course.getStartDate(),
				course.getEndDate());
		if (overlaps) {
			throw new IllegalStateException("Enrollment overlaps with another enrolled course for this student.");
		}

		Enrollment saved = enrollmentRepo.save(Enrollment.builder().student(student).course(course).build());
		log("ENROLLMENT_CREATE", saved.getId(), "SUCCESS", null, saved,
				Map.of("studentId", studentId, "courseId", courseId));
		return toDto(saved);
	}

	public List<EnrollmentResponse> listByStudent(UUID studentId) {
		var out = enrollmentRepo.findByStudent_Id(studentId).stream().map(EnrollmentService::toDto).toList();
		log("ENROLLMENT_LIST_BY_STUDENT", null, "SUCCESS", null, null,
				Map.of("studentId", studentId, "count", out.size()));
		return out;
	}

	public List<EnrollmentResponse> listByCourse(UUID courseId) {
		var out = enrollmentRepo.findByCourse_Id(courseId).stream().map(EnrollmentService::toDto).toList();
		log("ENROLLMENT_LIST_BY_COURSE", null, "SUCCESS", null, null,
				Map.of("courseId", courseId, "count", out.size()));
		return out;
	}
}
