package com.hsystems.lms.group.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Group implements Serializable {

  private static final long serialVersionUID = -282215938253191214L;

  private String id;

  private String name;

  private List<String> permissions;

  private School school;

  private User createdBy;

  private LocalDateTime createdDateTime;

  private User modifiedBy;

  private LocalDateTime modifiedDateTime;

  Group() {

  }

  Group(
      String id,
      String name,
      List<String> permissions,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.school = school;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private String name;

    private List<String> permissions;
    private School school;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder permissions(List<String> permissions) {
      this.permissions = permissions;
      return this;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Builder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdDateTime(LocalDateTime createdDateTime) {
      this.createdDateTime = createdDateTime;
      return this;
    }

    public Builder modifiedBy(User modifiedBy) {
      this.modifiedBy = modifiedBy;
      return this;
    }

    public Builder modifiedDateTime(LocalDateTime modifiedDateTime) {
      this.modifiedDateTime = modifiedDateTime;
      return this;
    }

    public Group build() {
      return new Group(
          this.id,
          this.name,
          this.permissions,
          this.school,
          this.createdBy,
          this.createdDateTime,
          this.modifiedBy,
          this.modifiedDateTime
      );
    }
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Enumeration<String> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public School getSchool() {
    return school;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }
}