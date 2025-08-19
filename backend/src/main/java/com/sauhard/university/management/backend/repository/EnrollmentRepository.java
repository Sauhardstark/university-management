package com.sauhard.university.management.backend.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sauhard.university.management.backend.entities.Enrollment;
import com.sauhard.university.management.backend.repository.projections.CourseAggregate;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

	Enrollment findByStudent_IdAndCourse_Id(UUID studentId, UUID courseId);

	@Query("""
			select (count(e) > 0) from Enrollment e
			where e.student.id = :studentId
			  and e.course.id <> :courseId
			  and :newStart <= e.course.endDate
			  and :newEnd   >= e.course.startDate
			""")
	boolean existsOverlapWithOtherCourses(UUID studentId, UUID courseId, LocalDate newStart, LocalDate newEnd);

	List<Enrollment> findByStudent_Id(UUID studentId);

	List<Enrollment> findByCourse_Id(UUID courseId);

	// Courses page
	@Query("""
			select e.course.id as courseId,
			       avg(e.grade) as avgGrade,
			       sum(case when e.grade is not null and e.grade < :fail then 1 else 0 end) as failCount,
			       count(e) as enrollmentCount
			from Enrollment e
			group by e.course.id
			""")
	List<CourseAggregate> aggregateAll(@Param("fail") int failThreshold);

	// Teacher page
	@Query("""
			select e.course.id as courseId,
			       avg(e.grade) as avgGrade,
			       sum(case when e.grade is not null and e.grade < :fail then 1 else 0 end) as failCount,
			       count(e) as enrollmentCount
			from Enrollment e join e.course c
			where c.teacher.id = :teacherId
			group by e.course.id
			""")
	List<CourseAggregate> aggregateForTeacher(@Param("teacherId") UUID teacherId, @Param("fail") int failThreshold);

	// Batch
	@Query("""
			select e.course.id as courseId,
			       avg(e.grade) as avgGrade,
			       sum(case when e.grade is not null and e.grade < :fail then 1 else 0 end) as failCount,
			       count(e) as enrollmentCount
			from Enrollment e
			where e.course.id in :courseIds
			group by e.course.id
			""")
	List<CourseAggregate> aggregateByCourseIds(@Param("courseIds") Collection<UUID> courseIds,
			@Param("fail") int failThreshold);
	
	boolean existsByStudent_IdAndCourse_Id(UUID studentId, UUID courseId);

}