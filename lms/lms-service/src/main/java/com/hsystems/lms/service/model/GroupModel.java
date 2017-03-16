package com.hsystems.lms.service.model;

import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class GroupModel implements Serializable {

  private static final long serialVersionUID = 8759461339462158047L;

  private String id;

  private String name;

  private List<String> permissions;

  private List<UserModel> members;

  GroupModel() {

  }

  public GroupModel(
      String id,
      String name,
      List<String> permissions,
      List<UserModel> members) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.members = members;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPermissions() {
    return ListUtils.isEmpty(permissions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(permissions);
  }

  public void setPermissions(List<String> permissions) {
    if (ListUtils.isEmpty(permissions)) {
      permissions = new ArrayList<>();
    }

    this.permissions.clear();
    this.permissions.addAll(permissions);
  }

  public List<UserModel> getMembers() {
    return ListUtils.isEmpty(members)
        ? Collections.emptyList()
        : Collections.unmodifiableList(members);
  }

  public void setMembers(List<UserModel> users) {
    if (ListUtils.isEmpty(users)) {
      this.members = new ArrayList<>();
    }

    this.members.clear();
    this.members.addAll(users);
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

    GroupModel model = (GroupModel) obj;
    return id.equals(model.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Group{id=%s, name=%s, users=%s}",
        id, name, StringUtils.join(members, ","));
  }
}