package com.devamy.dcombat.util;

import java.time.Duration;

public class DurationUtil {

    public static final Duration ONE_SECOND = Duration.ofSeconds(1);

    public DurationUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String format(Duration duration, boolean removeMillis) {
        if (removeMillis) {
            if (duration.toMillis() < ONE_SECOND.toMillis()) {
                return "0s";
            }

            return formatWithoutMillis(duration.plusMillis(999));
        }

        if (duration.toMillis() > ONE_SECOND.toMillis()) {
            return formatWithoutMillis(duration.plusMillis(999));
        }

        return formatWithMillis(duration);
    }

    public static String format(Duration duration) {
        return format(duration, true);
    }

    private static String formatWithoutMillis(Duration duration) {
        long seconds = Math.max(0L, duration.toSeconds());
        long days = seconds / 86_400L;
        seconds %= 86_400L;
        long hours = seconds / 3_600L;
        seconds %= 3_600L;
        long minutes = seconds / 60L;
        seconds %= 60L;

        StringBuilder builder = new StringBuilder();
        append(builder, days, "d");
        append(builder, hours, "h");
        append(builder, minutes, "m");
        append(builder, seconds, "s");

        if (builder.isEmpty()) {
            return "0s";
        }

        return builder.toString();
    }

    private static String formatWithMillis(Duration duration) {
        long millis = Math.max(0L, duration.toMillis());
        long seconds = millis / 1000L;
        millis %= 1000L;

        StringBuilder builder = new StringBuilder();
        append(builder, seconds, "s");
        append(builder, millis, "ms");

        if (builder.isEmpty()) {
            return "0ms";
        }

        return builder.toString();
    }

    private static void append(StringBuilder builder, long value, String unit) {
        if (value <= 0L) {
            return;
        }

        builder.append(value).append(unit);
    }
}
