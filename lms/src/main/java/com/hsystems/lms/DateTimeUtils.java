package com.hsystems.lms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateTimeUtils {

  private static final DateTimeFormatter formatter
      = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String toString(LocalDate date) {
    return date.format(formatter);
  }

  public static LocalDateTime toLocalDateTime(String value) {
    return LocalDateTime.parse(value, formatter);
  }
}
