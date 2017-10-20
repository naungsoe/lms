package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.User;

/**
 * Created by naungsoe on 4/11/16.
 */
public class Configuration {

  private final String dateFormat;

  private final String dateTimeFormat;

  Configuration(
      String dateFormat,
      String dateTimeFormat) {

    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
  }

  public static Configuration create() {
    return new Configuration(
        Constants.FORMAT_DATE,
        Constants.FORMAT_DATE_TIME
    );
  }

  public static Configuration create(User user) {
    return new Configuration(
        user.getDateFormat(),
        user.getDateTimeFormat()
    );
  }

  public static Configuration create(Principal principal) {
    return new Configuration(
        principal.getDateFormat(),
        principal.getDateTimeFormat()
    );
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }
}
