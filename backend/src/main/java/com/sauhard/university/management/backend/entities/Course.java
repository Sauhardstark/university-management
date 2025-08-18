package com.sauhard.university.management.backend.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(name = "courses_code_start", columnNames = { "code",
		"start_date" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@NotBlank
	@Column(nullable = false)
	private String code;

	@NotBlank
	@Column(nullable = false)
	private String name;

	@NotNull
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@NotNull
	@Column(name = "end_date")
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@PrePersist
	@PreUpdate
	private void validateDates() {
		if (endDate != null && endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("endDate cannot be before startDate");
		}
	}
}
