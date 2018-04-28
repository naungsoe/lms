package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.entity.User;

/**
 * Created by naungsoe on 4/11/16.
 */
public class Configuration {

  public static final String TIME_FORMAT = "HH:mm:ss.SSS";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  private final String timeFormat;

  private final String dateFormat;

  private final String dateTimeFormat;

  Configuration(
      String timeFormat,
      String dateFormat,
      String dateTimeFormat) {

    this.timeFormat = timeFormat;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
  }

  public static Configuration create() {
    return new Configuration(
        TIME_FORMAT,
        DATE_FORMAT,
        DATE_TIME_FORMAT
    );
  }

  public static Configuration create(User user) {
    return new Configuration(
        user.getTimeFormat(),
        user.getDateFormat(),
        user.getDateTimeFormat()
    );
  }

  public static Configuration create(Principal principal) {
    return new Configuration(
        principal.getTimeFormat(),
        principal.getDateFormat(),
        principal.getDateTimeFormat()
    );
  }

  public String getTimeFormat() {
    return timeFormat;
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }
}