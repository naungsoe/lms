package com.hsystems.lms.service.mapper;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.model.UserModel;

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
        Constants.DATE_FORMAT,
        Constants.DATE_TIME_FORMAT
    );
  }

  public static Configuration create(User user) {
    return new Configuration(
        user.getDateFormat(),
        user.getDateTimeFormat()
    );
  }

  public static Configuration create(UserModel userModel) {
    return new Configuration(
        userModel.getDateFormat(),
        userModel.getDateTimeFormat()
    );
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }
}
