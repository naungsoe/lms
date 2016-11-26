package com.hsystems.lms.service.model;

import com.hsystems.lms.common.Permission;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class SchoolModel implements Serializable {

  private static final long serialVersionUID = -8371629223750518583L;

  private String id;

  private String name;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<Permission> permissions;

  SchoolModel() {

  }

  public SchoolModel(
      String id,
      String name,
      String locale,
      String dateFormat,
      String dateTimeFormat,
      List<Permission> permissions) {

    this.id = id;
    this.name = name;
    this.locale = locale;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.permissions = permissions;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLocale() { return locale; }

  public String getDateFormat() {
    return dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public List<Permission> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }
}
