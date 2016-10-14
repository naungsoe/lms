package com.hsystems.lms.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by administrator on 7/10/16.
 */
public class School implements Serializable {

  private static final long serialVersionUID = -8371629223750518583L;

  protected String id;

  protected String name;

  protected String locale;

  protected List<Permission> permissions;

  School() {

  }

  public School(
      String id,
      String name,
      String locale) {

    this.id = id;
    this.name = name;
    this.locale = locale;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLocale() { return locale; }

  public List<Permission> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }
}
