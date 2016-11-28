package com.hsystems.lms.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateTimeUtils {

  private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static String toString(LocalDateTime dateTime) {
    return toString(dateTime, DEFAULT_FORMAT);
  }

  public static String toString(LocalDateTime dateTime, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return dateTime.format(formatter);
  }

  public static LocalDateTime toLocalDateTime(String value) {
    return toLocalDateTime(value, DEFAULT_FORMAT);
  }

  public static LocalDateTime toLocalDateTime(String value, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(value, formatter);
  }

  public static LocalDateTime toLocalDateTime(long value) {
    return Instant.ofEpochMilli(value)
        .atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static long getCurrentMilliseconds() {
    return LocalDateTime.now().atZone(ZoneId.systemDefault())
        .toInstant().toEpochMilli();
  }
}
