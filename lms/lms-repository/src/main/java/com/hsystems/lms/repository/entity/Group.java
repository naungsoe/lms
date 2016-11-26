package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
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
      String name,
      List<Permission> permissions,
      School school,
      List<User> members) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.members = members;
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
}
