package com.hsystems.lms.repository.entity.assignment;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.User;

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
@IndexCollection(namespace = "lms", name = "assignments")
public abstract class Assignment implements Entity, Auditable, Serializable {

  @IndexField
  protected String id;

  @IndexField
  protected List<Group> groups;

  @IndexField
  protected LocalDateTime startDateTime;

  @IndexField
  protected LocalDateTime endDateTime;

  @IndexField
  protected LocalDateTime releaseDateTime;

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

  public Enumeration<Group> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(groups);
  }

  public void addGroup(Group... groups) {
    if (CollectionUtils.isEmpty(this.groups)) {
      this.groups = new ArrayList<>();
    }

    Arrays.stream(groups).forEach(group -> {
      checkEmptyGroup(group);
      this.groups.add(group);
    });
  }

  private void checkEmptyGroup(Group group) {
    Enumeration<User> members = group.getMembers();
    CommonUtils.checkArgument(members.hasMoreElements(), "group is empty");
  }

  public void removeGroup(Group group) {
    checkEmptyGroup(group);
    this.groups.remove(group);
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public LocalDateTime getReleaseDateTime() {
    return releaseDateTime;
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
