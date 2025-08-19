package com.sauhard.university.management.backend.logging;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sauhard.university.management.backend.dto.LogEvent;
import com.sauhard.university.management.backend.entities.SystemLog;
import com.sauhard.university.management.backend.repository.SystemLogRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogEventListener {

	private final SystemLogRepository repository;
	private final ObjectMapper objectMapper;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
	public void handle(LogEvent e) {
		var ctx = LoggingRequestContext.get();
		JsonNode oldNode = toNode(e.oldData());
		JsonNode newNode = toNode(e.newData());
		JsonNode metaNode = toNode(e.metadata());

		var log = SystemLog.builder().action(e.action()).entityType(e.entityType()).entityId(e.entityId())
				.actorId(ctx != null ? ctx.getActorId() : null).requestId(ctx != null ? ctx.getRequestId() : null)
				.httpMethod(ctx != null ? ctx.getHttpMethod() : null).route(ctx != null ? ctx.getRoute() : null)
				.status(e.status()).oldData(oldNode).newData(newNode).metadata(metaNode).build();

		repository.save(log);
	}

	@EventListener
	public void handleNonTx(LogEvent e) {
	}

	private JsonNode toNode(Object o) {
		return o == null ? null : objectMapper.valueToTree(o);
	}
}
