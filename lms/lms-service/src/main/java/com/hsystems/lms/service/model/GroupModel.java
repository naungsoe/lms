package com.hsystems.lms.service.model;

import com.hsystems.lms.common.util.CollectionUtils;

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

  public GroupModel() {

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
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(permissions);
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = new ArrayList<>();
    this.permissions.addAll(permissions);
  }

  public List<UserModel> getMembers() {
    return CollectionUtils.isEmpty(members)
        ? Collections.emptyList()
        : Collections.unmodifiableList(members);
  }

  public void setMembers(List<UserModel> users) {
    this.members = new ArrayList<>();
    this.members.addAll(users);
  }
}
