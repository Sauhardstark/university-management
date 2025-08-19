package com.sauhard.university.management.backend.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sauhard.university.management.backend.entities.Course;
import com.sauhard.university.management.backend.entities.Enrollment;
import com.sauhard.university.management.backend.entities.Student;
import com.sauhard.university.management.backend.entities.Teacher;
import com.sauhard.university.management.backend.repository.CourseRepository;
import com.sauhard.university.management.backend.repository.EnrollmentRepository;
import com.sauhard.university.management.backend.repository.StudentRepository;
import com.sauhard.university.management.backend.repository.TeacherRepository;

@Component
public class DataInitializer implements CommandLineRunner {

  private final StudentRepository students;
  private final TeacherRepository teachers;
  private final CourseRepository courses;
  private final EnrollmentRepository enrollments;

  public DataInitializer(StudentRepository students, TeacherRepository teachers,
                         CourseRepository courses, EnrollmentRepository enrollments) {
    this.students = students;
    this.teachers = teachers;
    this.courses = courses;
    this.enrollments = enrollments;
  }

@Override
public void run(String... args) {
  // helpers: get-or-create by unique fields
  Teacher hop = teachers.findByEmail("grace@uni.edu")
      .orElseGet(() -> teachers.save(Teacher.builder().firstName("Grace").lastName("Hopper").email("grace@uni.edu").build()));
  Teacher eds = teachers.findByEmail("edsger@uni.edu")
      .orElseGet(() -> teachers.save(Teacher.builder().firstName("Edsger").lastName("Dijkstra").email("edsger@uni.edu").build()));
  Teacher bar = teachers.findByEmail("barbara@uni.edu")
      .orElseGet(() -> teachers.save(Teacher.builder().firstName("Barbara").lastName("Liskov").email("barbara@uni.edu").build()));

  Student ada  = students.findByEmail("ada@uni.edu")
      .orElseGet(() -> students.save(Student.builder().firstName("Ada").lastName("Lovelace").email("ada@uni.edu").build()));
  Student alan = students.findByEmail("alan@uni.edu")
      .orElseGet(() -> students.save(Student.builder().firstName("Alan").lastName("Turing").email("alan@uni.edu").build()));
  Student kat  = students.findByEmail("katherine@uni.edu")
      .orElseGet(() -> students.save(Student.builder().firstName("Katherine").lastName("Johnson").email("katherine@uni.edu").build()));
  Student don  = students.findByEmail("donald@uni.edu")
      .orElseGet(() -> students.save(Student.builder().firstName("Donald").lastName("Knuth").email("donald@uni.edu").build()));

  var F25s = LocalDate.of(2025, 9, 1);
  var F25e = LocalDate.of(2025, 12, 15);
  var S26s = LocalDate.of(2026, 1, 10);
  var S26e = LocalDate.of(2026, 5, 10);

  Course cs101_f25 = courses.findByCodeAndStartDate("CS101", F25s).orElseGet(() ->
      courses.save(Course.builder().code("CS101").name("Intro to CS").startDate(F25s).endDate(F25e).teacher(hop).build()));
  Course cs201_f25 = courses.findByCodeAndStartDate("CS201", F25s).orElseGet(() ->
      courses.save(Course.builder().code("CS201").name("Algorithms").startDate(F25s).endDate(F25e).teacher(eds).build()));
  Course cs202_f25 = courses.findByCodeAndStartDate("CS202", F25s).orElseGet(() ->
      courses.save(Course.builder().code("CS202").name("Data Structures").startDate(F25s).endDate(F25e).teacher(eds).build()));
  Course cs301_f25 = courses.findByCodeAndStartDate("CS301", F25s).orElseGet(() ->
      courses.save(Course.builder().code("CS301").name("Databases").startDate(F25s).endDate(F25e).teacher(bar).build()));
  Course cs401_s26 = courses.findByCodeAndStartDate("CS401", S26s).orElseGet(() ->
      courses.save(Course.builder().code("CS401").name("Compilers").startDate(S26s).endDate(S26e).teacher(hop).build()));

  // enroll if missing
  if (!enrollments.existsByStudent_IdAndCourse_Id(ada.getId(), cs101_f25.getId()))
    enrollments.save(Enrollment.builder().student(ada).course(cs101_f25).grade(96).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(alan.getId(), cs201_f25.getId()))
    enrollments.save(Enrollment.builder().student(alan).course(cs201_f25).grade(88).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(alan.getId(), cs401_s26.getId()))
    enrollments.save(Enrollment.builder().student(alan).course(cs401_s26).grade(91).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(kat.getId(), cs202_f25.getId()))
    enrollments.save(Enrollment.builder().student(kat).course(cs202_f25).grade(77).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(kat.getId(), cs301_f25.getId()))
    enrollments.save(Enrollment.builder().student(kat).course(cs301_f25).grade(84).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(don.getId(), cs101_f25.getId()))
    enrollments.save(Enrollment.builder().student(don).course(cs101_f25).grade(59).build());
  if (!enrollments.existsByStudent_IdAndCourse_Id(don.getId(), cs301_f25.getId()))
    enrollments.save(Enrollment.builder().student(don).course(cs301_f25).grade(72).build());
}

}
