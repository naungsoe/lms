package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by naungsoe on 7/10/16.
 */
@XmlRootElement
public class School extends Auditable implements Serializable {

  private static final long serialVersionUID = -8371629223750518583L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.TEXT_WHITE_SPACE)
  private String name;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<Permission> permissions;

  School() {

  }

  public School(
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
