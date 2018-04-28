package com.hsystems.lms.common.security;

/**
 * Created by naungsoe on 30/12/16.
 */
public interface Principal {

  String getId();

  String getFirstName();

  String getLastName();

  String getLocale();

  String getTimeFormat();

  String getDateFormat();

  String getDateTimeFormat();

  boolean hasPermission(String permission);
}
