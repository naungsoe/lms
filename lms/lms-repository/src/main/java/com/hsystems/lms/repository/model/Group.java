package com.hsystems.lms.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class Group implements Serializable {

  private static final long serialVersionUID = 2420329732282197342L;

  private String id;

  private String name;

  private List<Permission> permissions;

  private School school;

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
