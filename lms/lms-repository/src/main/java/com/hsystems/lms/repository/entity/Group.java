package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(name = "groups")
public class Group extends Auditable implements Entity, Serializable {

  private static final long serialVersionUID = 2420329732282197342L;

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

  public List<Permission> getPermissions() {
    return ListUtils.isEmpty(permissions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(permissions);
  }

  public List<User> getMembers() {
    return ListUtils.isEmpty(members)
        ? Collections.emptyList()
        : Collections.unmodifiableList(members);
  }

  public School getSchool() {
    return school;
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
        StringUtils.join(members, ","), school,
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
