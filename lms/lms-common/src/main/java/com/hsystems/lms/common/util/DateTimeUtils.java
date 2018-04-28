package com.hsystems.lms.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateTimeUtils {

  private static final String TIME_FORMAT = "HH:mm:ss.SSS";
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static String toString(LocalTime time) {
    return toString(time, TIME_FORMAT);
  }

  public static String toString(LocalTime time, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return time.format(formatter);
  }

  public static String toString(LocalDate date) {
    return toString(date, DATE_FORMAT);
  }

  public static String toString(LocalDate date, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return date.format(formatter);
  }

  public static String toString(LocalDateTime dateTime) {
    return toString(dateTime, DATETIME_FORMAT);
  }

  public static String toString(LocalDateTime dateTime, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return dateTime.format(formatter);
  }

  public static LocalDateTime toLocalDateTime(String value) {
    return toLocalDateTime(value, DATETIME_FORMAT);
  }

  public static LocalDateTime toLocalDateTime(String value, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(value, formatter);
  }

  public static LocalTime toLocalTime(long value) {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault()).toLocalTime();
  }

  public static LocalDate toLocalDate(long value) {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDateTime toLocalDateTime(long value) {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static long getCurrentMilliseconds() {
    return getMilliseconds(LocalDateTime.now());
  }

  public static long getMilliseconds(LocalDateTime dateTime) {
    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  public static boolean isEmpty(LocalDateTime dateTime) {
    return dateTime == null;
  }

  public static boolean isNotEmpty(LocalDateTime dateTime) {
    return !isEmpty(dateTime);
  }

  public static boolean isToday(LocalDateTime dateTime) {
    LocalDateTime now = LocalDateTime.now();
    return ChronoUnit.HOURS.between(dateTime, now) > 24;
  }
}