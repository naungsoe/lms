package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public abstract class Resource implements Entity, Auditable, Serializable {

  @IndexField
  protected String id;

  @IndexField
  protected School school;

  @IndexField
  protected List<Level> levels;

  @IndexField
  protected List<Subject> subjects;

  @IndexField
  protected List<String> keywords;

  @IndexField
  protected List<ResourcePermission> permissions;

  @IndexField
  protected User createdBy;

  @IndexField
  protected LocalDateTime createdDateTime;

  @IndexField
  protected User modifiedBy;

  @IndexField
  protected LocalDateTime modifiedDateTime;

  @Override
  public String getId() {
    return id;
  }

  public School getSchool() {
    return school;
  }

  public Enumeration<Level> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(levels);
  }

  public Enumeration<Subject> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(subjects);
  }

  public Enumeration<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(keywords);
  }

  public Enumeration<ResourcePermission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public void addPermission(ResourcePermission... permissions) {
    if (CollectionUtils.isEmpty(this.permissions)) {
      this.permissions = new ArrayList<>();
    }

    Arrays.stream(permissions).forEach(this.permissions::add);
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
}
