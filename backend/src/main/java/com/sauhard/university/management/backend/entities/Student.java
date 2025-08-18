package com.sauhard.university.management.backend.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(name = "students_email", columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@NotBlank
	@Column(nullable = false)
	private String firstName;

	@NotBlank
	@Column(nullable = false)
	private String lastName;

	@Email
	@NotBlank
	@Column(nullable = false)
	private String email;
}
