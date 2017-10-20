package com.hsystems.lms.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

  public static String toPrettyTime(LocalDateTime dateTime, String format) {
    LocalDateTime now = LocalDateTime.now();
    long hours = ChronoUnit.HOURS.between(dateTime, now);

    if (hours > 23) {
      return toString(dateTime, format);
    }

    long minutes = ChronoUnit.MINUTES.between(dateTime, now);
    long seconds = ChronoUnit.SECONDS.between(dateTime, now);

    if (hours > 0) {
      String suffix = (hours == 1)
          ? " hour ago" : " hours ago";
      return hours + suffix;

    } else if (minutes > 0) {
      String suffix = (minutes == 1)
          ? " minutes ago" : " minutes ago";
      return minutes + suffix;

    } else {
      String suffix = (seconds == 1)
          ? " seconds ago" : " seconds ago";
      return seconds + suffix;
    }
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
