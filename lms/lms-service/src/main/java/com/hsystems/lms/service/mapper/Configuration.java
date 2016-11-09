package com.hsystems.lms.service.mapper;

import com.hsystems.lms.repository.model.User;

/**
 * Created by naungsoe on 4/11/16.
 */
public class Configuration {

  private String dateFormat;

  private String dateTimeFormat;

  Configuration(
      String dateFormat,
      String dateTimeFormat) {

    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
  }

  public static Configuration create(User user) {
    return new Configuration(
        user.getDateFormat(),
        user.getDateTimeFormat()
    );
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }
}