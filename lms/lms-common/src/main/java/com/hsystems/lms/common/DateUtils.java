package com.hsystems.lms.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by naungsoe on 10/9/16.
 */
public final class DateUtils {

  private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

  public static String toString(LocalDate date) {
    return toString(date, DEFAULT_FORMAT);
  }

  public static String toString(LocalDate date, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return date.format(formatter);
  }

  public static LocalDate toLocalDate(String value) {
    return toLocalDate(value, DEFAULT_FORMAT);
  }

  public static LocalDate toLocalDate(String value, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDate.parse(value, formatter);
  }
}
