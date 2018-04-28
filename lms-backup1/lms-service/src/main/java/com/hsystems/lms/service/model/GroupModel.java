package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class GroupModel extends AuditableModel
    implements Serializable {

  private static final long serialVersionUID = 1537467734571464015L;

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
        ? Collections.emptyList() : permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = new ArrayList<>();
    this.permissions.addAll(permissions);
  }

  public List<UserModel> getMembers() {
    return CollectionUtils.isEmpty(members)
        ? Collections.emptyList() : members;
  }

  public void setMembers(List<UserModel> users) {
    this.members = new ArrayList<>();
    this.members.addAll(users);
  }
}