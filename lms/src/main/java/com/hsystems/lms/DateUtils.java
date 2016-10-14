package com.hsystems.lms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by administrator on 10/9/16.
 */
public final class DateUtils {

  private static final DateTimeFormatter formatter
      = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String toString(LocalDate date) {
    return date.format(formatter);
  }

  public static LocalDate toLocalDate(String value) {
    return LocalDate.parse(value, formatter);
  }
}
