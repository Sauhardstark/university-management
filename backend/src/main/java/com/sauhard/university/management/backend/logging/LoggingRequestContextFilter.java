package com.sauhard.university.management.backend.logging;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingRequestContextFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		UUID requestId = UUID.randomUUID();
		// This actor id should come from auth which tells us about the caller so for now
		// I'm keeping it to random
		UUID actorId = UUID.randomUUID();
		var data = LoggingRequestContext.Data.builder().requestId(requestId).actorId(actorId)
				.httpMethod(req.getMethod()).route(req.getRequestURI()).build();
		LoggingRequestContext.set(data);
		try {
			chain.doFilter(req, res);
		} finally {
			LoggingRequestContext.clear();
		}
	}
}
