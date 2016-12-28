package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class Group extends Auditable implements Serializable {

  private static final long serialVersionUID = 2420329732282197342L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.TEXT_WHITE_SPACE)
  private String name;

  private List<Permission> permissions;

  @IndexField(type = IndexFieldType.OBJECT)
  private School school;

  @IndexField(type = IndexFieldType.LIST)
  private List<User> members;

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
      School school,
      List<User> members,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.school = school;
    this.members = members;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Permission> getPermissions() {
    return Collections.unmodifiableList(permissions);
  }

  public School getSchool() { return  school; }

  public List<User> getMembers() {
    return Collections.unmodifiableList(members);
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
    StringBuilder permissionsBuilder = new StringBuilder();
    permissions.forEach(x -> permissionsBuilder.append(x).append(","));

    StringBuilder membersBuilder = new StringBuilder();
    members.forEach(x -> membersBuilder.append(x).append(","));

    return String.format(
        "Group{id=%s, name=%s, permissions=%s, school=%s, "
            + "members=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, name, permissionsBuilder, school, membersBuilder,
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
