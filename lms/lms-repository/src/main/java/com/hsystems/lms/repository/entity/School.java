package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by naungsoe on 7/10/16.
 */
@XmlRootElement
public class School extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = -8371629223750518583L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.TEXT_WHITE_SPACE)
  private String name;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<Permission> permissions;

  private List<Group> groups;

  School() {

  }

  public School(
      String id,
      String name) {

    this.id = id;
    this.name = name;
  }


  public School(
      String id,
      String name,
      String locale,
      String dateFormat,
      String dateTimeFormat,
      List<Permission> permissions,
      List<Group> groups,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.locale = locale;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.permissions = permissions;
    this.groups = groups;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
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

  public List<Group> getGroups() {
    return Collections.unmodifiableList(groups);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    School school = (School) obj;
    return id.equals(school.getId());
  }

  @Override
  public String toString() {
    StringBuilder permissionsBuilder = new StringBuilder();
    permissions.forEach(x -> permissionsBuilder.append(x).append(","));

    StringBuilder groupsBuilder = new StringBuilder();
    groups.forEach(x -> groupsBuilder.append(x).append(","));

    return String.format(
        "School{id=%s, name=%s, locale=%s, dateFormat=%s, "
            + "dateTimeFormat=%s, permissions=%s, groups=%s, "
            + "createdBy=%s, createdDateTime=%s, modifiedBy=%s, "
            + "modifiedDateTime=%s}",
        id, name, locale, dateFormat, dateTimeFormat, permissions,
        groupsBuilder, createdBy, createdDateTime, modifiedBy,
        modifiedDateTime);
  }
}
