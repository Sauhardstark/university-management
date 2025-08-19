package com.sauhard.university.management.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sauhard.university.management.backend.entities.SystemLog;

public interface SystemLogRepository extends JpaRepository<SystemLog, UUID> {
}
