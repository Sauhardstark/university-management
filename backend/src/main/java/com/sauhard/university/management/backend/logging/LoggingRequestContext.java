package com.sauhard.university.management.backend.logging;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

public final class LoggingRequestContext {
    @Value
    @Builder
    public static class Data {
        UUID requestId;
        UUID actorId;
        String httpMethod;
        String route;
    }

    private static final ThreadLocal<Data> CTX = new ThreadLocal<>();

    public static void set(Data data) { CTX.set(data); }
    public static Data get() { return CTX.get(); }
    public static void clear() { CTX.remove(); }
}
