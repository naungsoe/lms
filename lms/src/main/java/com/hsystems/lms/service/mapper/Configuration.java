package com.hsystems.lms.service.mapper;

import com.hsystems.lms.model.School;

/**
 * Created by naungsoe on 4/11/16.
 */
public class Configuration {

  private String dateFormat;

  private String dateTimeFormat;

  Configuration(
      String dateFormat,
      String dateTimeForat) {

    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
  }

  public static Configuration create(School school) {
    return new Configuration(
        school.getDateFormat(),
        school.getDateTimeFormat()
    );
  }
}
