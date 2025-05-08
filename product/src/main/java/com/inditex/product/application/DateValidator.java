package com.inditex.product.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

public static boolean isValidTimestamp(String timestamp) {
    try {
        LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
        return true;
    } catch (DateTimeParseException e) {
        return false;
    }
}

}
