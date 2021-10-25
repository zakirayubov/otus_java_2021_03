package ru.otus.util;

import java.util.concurrent.TimeUnit;

public final class Utils {
    private Utils() {
    }

    public static void sleep(long durationInSeconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(durationInSeconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
