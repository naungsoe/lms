package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(namespace = "lms", name = "groups")
public class Group implements Entity, Auditable, Serializable {

  private static final long serialVersionUID = 8449941063254753142L;

  @IndexField
  private String id;

  @IndexField
  private String name;

  @IndexField
  private List<Permission> permissions;

  @IndexField
  private List<User> members;

  @IndexField
  private School school;

  @IndexField
  private User createdBy;

  @IndexField
  private LocalDateTime createdDateTime;

  @IndexField
  private User modifiedBy;

  @IndexField
  private LocalDateTime modifiedDateTime;

  Group() {

  }

  public Group(
      String id,
      String name) {

    this.id = id;
    this.name = name;
  }

  public Group(
      String id,
      String name,
      List<Permission> permissions,
      List<User> members,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.members = members;
    this.school = school;
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

  public Enumeration<Permission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public Enumeration<User> getMembers() {
    return CollectionUtils.isEmpty(members)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(members);
  }

  public School getSchool() {
    return school;
  }

  @Override
  public User getCreatedBy() {
    return createdBy;
  }

  @Override
  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  @Override
  public User getModifiedBy() {
    return modifiedBy;
  }

  @Override
  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
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

    Group group = (Group) obj;
    return id.equals(group.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Group{id=%s, name=%s, permissions=%s, members=%s, "
            + "school=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, name, StringUtils.join(permissions, ","),
        StringUtils.join(members, ","), school, createdBy,
        createdDateTime, modifiedBy, modifiedDateTime);
  }
}
