package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class DateTimeProvider {

    private final DateTime dateTime;

    public DateTimeProvider(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDate() {
        return dateTime.getDate();
    }
}
