package com.sauhard.university.management.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sauhard.university.management.backend.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

	@EntityGraph(attributePaths = "teacher")
	@Query("select c from Course c")
	List<Course> findAllWithTeacher();

	@EntityGraph(attributePaths = "teacher")
	List<Course> findByTeacher_Id(UUID teacherId);

	List<Course> findByNameContainingIgnoreCase(String namePart);
	Optional<Course> findByCodeAndStartDate(String code, LocalDate startDate);
}
