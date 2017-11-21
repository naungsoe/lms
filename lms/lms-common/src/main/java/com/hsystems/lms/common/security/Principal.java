package com.hsystems.lms.common.security;

/**
 * Created by naungsoe on 30/12/16.
 */
public interface Principal {

  String getId();

  String getName();

  String getLocale();

  String getTimeFormat();

  String getDateFormat();

  String getDateTimeFormat();

  boolean hasPermission(String permission);
}
