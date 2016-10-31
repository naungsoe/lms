package com.hsystems.lms.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by naungsoe on 7/10/16.
 */
@XmlRootElement
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
      String locale,
      List<Permission> permissions) {

    this.id = id;
    this.name = name;
    this.locale = locale;
    this.permissions = permissions;
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
