package com.hsystems.lms.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateTimeUtils {

  private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";

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
}
