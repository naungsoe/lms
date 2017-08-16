package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;

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
public abstract class ResourceAssignment 
		extends Auditable implements Serializable {

  @IndexField
  protected List<Group> groups;

  @IndexField
  protected LocalDateTime startDateTime;

  @IndexField
  protected LocalDateTime endDateTime;

  @IndexField
  protected LocalDateTime releaseDateTime;

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
}
