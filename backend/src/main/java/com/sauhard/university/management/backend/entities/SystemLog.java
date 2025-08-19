package com.sauhard.university.management.backend.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {
	@Id
	@GeneratedValue
	private UUID id;

	@CreationTimestamp
	private Instant ts;

	@Column(nullable = false)
	private String action;

	@Column(name = "entity_type", nullable = false)
	private String entityType;

	@Column(name = "entity_id")
	private UUID entityId;

	@Column(name = "actor_id")
	private UUID actorId;

	@Column(name = "request_id")
	private UUID requestId;

	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "route")
	private String route;

	@Column(nullable = false)
	private String status;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private JsonNode oldData;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private JsonNode newData;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private JsonNode metadata;
}
