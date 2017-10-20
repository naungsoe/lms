package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.entity.Assignment;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.School;
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
public final class LessonAssignment
    implements Assignment, Auditable, Serializable {

  private static final long serialVersionUID = 7183749866539443478L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  @IndexField
  private List<Group> groups;

  @IndexField
  private boolean componentsShuffled;

  @IndexField
  private long durationAllowed;

  @IndexField
  private int attemptsAllowed;

  @IndexField
  private LocalDateTime startDateTime;

  @IndexField
  private LocalDateTime endDateTime;

  @IndexField
  private LocalDateTime releaseDateTime;

  @IndexField
  private School school;

  @IndexField
  private User createdBy;

  @IndexField
  private LocalDateTime createdDateTime;

  @IndexField
  private User modifiedBy;

  @IndexField
  private LocalDateTime modifiedDateTime;

  LessonAssignment() {

  }

  LessonAssignment(
      String id,
      Lesson lesson,
      List<Group> groups,
      boolean componentsShuffled,
      long durationAllowed,
      int attemptsAllowed,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      LocalDateTime releaseDateTime,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.lesson = lesson;
    this.groups = groups;
    this.groups = groups;
    this.componentsShuffled = componentsShuffled;
    this.durationAllowed =  durationAllowed;
    this.attemptsAllowed = attemptsAllowed;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.releaseDateTime = releaseDateTime;
    this.school = school;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private Lesson lesson;
    List<Group> groups;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime releaseDateTime;
    private boolean componentsShuffled;
    private long durationAllowed;
    private int attemptsAllowed;
    private School school;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, Lesson lesson, List<Group> groups) {
      this.id = id;
      this.lesson = lesson;
      this.groups = groups;
    }

    public Builder startDateTime(LocalDateTime startDateTime) {
      this.startDateTime = startDateTime;
      return this;
    }

    public Builder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      return this;
    }

    public Builder releaseDateTime(LocalDateTime releaseDateTime) {
      this.releaseDateTime = releaseDateTime;
      return this;
    }

    public Builder componentsShuffled(boolean componentsShuffled) {
      this.componentsShuffled = componentsShuffled;
      return this;
    }

    public Builder durationAllowed(long durationAllowed) {
      this.durationAllowed = durationAllowed;
      return this;
    }

    public Builder attemptsAllowed(int attemptsAllowed) {
      this.attemptsAllowed = attemptsAllowed;
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

    public LessonAssignment build() {
      return new LessonAssignment(
          this.id,
          this.lesson,
          this.groups,
          this.componentsShuffled,
          this.durationAllowed,
          this.attemptsAllowed,
          this.startDateTime,
          this.endDateTime,
          this.releaseDateTime,
          this.school,
          this.createdBy,
          this.createdDateTime,
          this.modifiedBy,
          this.modifiedDateTime
      );
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public Lesson getLesson() {
    return lesson;
  }

  @Override
  public Enumeration<Group> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(groups);
  }

  @Override
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

  @Override
  public void removeGroup(Group group) {
    checkEmptyGroup(group);
    this.groups.remove(group);
  }

  public boolean isComponentsShuffled() {
    return componentsShuffled;
  }

  public long getDurationAllowed() {
    return durationAllowed;
  }

  public int getAttemptsAllowed() {
    return attemptsAllowed;
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  @Override
  public LocalDateTime getReleaseDateTime() {
    return releaseDateTime;
  }

  @Override
  public School getSchool() {
    return school;
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
