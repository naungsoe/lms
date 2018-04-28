package com.hsystems.lms.school.repository.entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Preferences implements Serializable {

  private static final long serialVersionUID = 1135242822410059330L;

  private String locale;

  private String timeFormat;

  private String dateFormat;

  private String dateTimeFormat;

  Preferences() {

  }

  public Preferences(
      String locale,
      String timeFormat,
      String dateFormat,
      String dateTimeFormat) {

    this.locale = locale;
    this.timeFormat = timeFormat;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
  }

  public String getLocale() {
    return locale;
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