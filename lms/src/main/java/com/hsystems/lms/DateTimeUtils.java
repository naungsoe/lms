package com.hsystems.lms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateTimeUtils {

  private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";

  public static String toString(LocalDate date) {
    return toString(date, DEFAULT_FORMAT);
  }

  public static String toString(LocalDate date, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return date.format(formatter);
  }

  public static LocalDateTime toLocalDateTime(String value) {
    return toLocalDateTime(value, DEFAULT_FORMAT);
  }

  public static LocalDateTime toLocalDateTime(String value, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(value, formatter);
  }
}
