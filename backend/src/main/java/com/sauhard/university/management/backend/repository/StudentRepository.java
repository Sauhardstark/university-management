package com.sauhard.university.management.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sauhard.university.management.backend.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}