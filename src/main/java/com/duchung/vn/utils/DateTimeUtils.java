package com.duchung.vn.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeUtils {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private DateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatDate(LocalDate date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DEFAULT_DATETIME_PATTERN);
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseDate(String dateString) {
        return parseDate(dateString, DEFAULT_DATE_PATTERN);
    }

    public static LocalDate parseDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        return parseDateTime(dateTimeString, DEFAULT_DATETIME_PATTERN);
    }

    public static LocalDateTime parseDateTime(String dateTimeString, String pattern) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    public static LocalDateTime getCurrentUtcDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDate getCurrentUtcDate() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), DEFAULT_ZONE_ID);
    }

    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(23, 59, 59, 999999999);
    }
}