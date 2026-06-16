package com.devamy.dcombat.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class Delay<T> {

    private final Supplier<Duration> durationSupplier;
    private final Map<T, Instant> markedAt = new ConcurrentHashMap<>();

    private Delay(Supplier<Duration> durationSupplier) {
        this.durationSupplier = durationSupplier;
    }

    public static <T> Delay<T> withDefault(Supplier<Duration> durationSupplier) {
        return new Delay<>(durationSupplier);
    }

    public void markDelay(T key) {
        this.markedAt.put(key, Instant.now());
    }

    public void unmarkDelay(T key) {
        this.markedAt.remove(key);
    }

    public boolean hasDelay(T key) {
        Instant startedAt = this.markedAt.get(key);

        if (startedAt == null) {
            return false;
        }

        if (this.getRemaining(startedAt).isZero()) {
            this.markedAt.remove(key);
            return false;
        }

        return true;
    }

    public Duration getRemaining(T key) {
        Instant startedAt = this.markedAt.get(key);

        if (startedAt == null) {
            return Duration.ZERO;
        }

        return this.getRemaining(startedAt);
    }

    private Duration getRemaining(Instant startedAt) {
        Duration remaining = this.durationSupplier.get().minus(Duration.between(startedAt, Instant.now()));

        if (remaining.isNegative() || remaining.isZero()) {
            return Duration.ZERO;
        }

        return remaining;
    }
}
