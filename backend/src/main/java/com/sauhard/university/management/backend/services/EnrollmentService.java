package com.sauhard.university.management.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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

	public Enrollment enroll(UUID studentId, UUID courseId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));
		Course course = courseRepo.findById(courseId)
				.orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

		Enrollment existing = enrollmentRepo.findByStudent_IdAndCourse_Id(studentId, courseId);
		if (existing != null) {
			return existing;
		}

		boolean overlaps = enrollmentRepo.existsOverlapWithOtherCourses(studentId, courseId, course.getStartDate(),
				course.getEndDate());
		if (overlaps) {
			throw new IllegalStateException("Enrollment overlaps with another enrolled course for this student.");
		}
		return enrollmentRepo.save(Enrollment.builder().student(student).course(course).build());
	}

	public List<Enrollment> listByStudent(UUID studentId) {
		return enrollmentRepo.findByStudent_Id(studentId);
	}

	public List<Enrollment> listByCourse(UUID courseId) {
		return enrollmentRepo.findByCourse_Id(courseId);
	}
}
