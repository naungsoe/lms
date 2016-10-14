package com.hsystems.lms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by administrator on 3/10/16.
 */
public class DateTimeUtils {

  private static final DateTimeFormatter formatter
      = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static String getString(LocalDate date) {
    return date.format(formatter);
  }

  public static LocalDate getDate(String value) {
    return LocalDate.parse(value, formatter);
  }
}
