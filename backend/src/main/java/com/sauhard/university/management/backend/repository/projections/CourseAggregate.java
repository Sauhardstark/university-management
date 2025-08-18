package com.sauhard.university.management.backend.repository.projections;

import java.util.UUID;

public interface CourseAggregate {
	UUID getCourseId();

	Double getAvgGrade();

	Long getFailCount();

	Long getEnrollmentCount();
}