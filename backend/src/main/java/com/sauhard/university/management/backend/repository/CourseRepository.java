package com.sauhard.university.management.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sauhard.university.management.backend.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
	Optional<Course> findByCode(String code);

	boolean existsByCode(String code);
}
