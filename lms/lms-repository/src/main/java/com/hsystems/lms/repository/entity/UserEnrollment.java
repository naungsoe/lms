package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.course.Course;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@IndexCollection(namespace = "lms", name = "enrollments")
public final class UserEnrollment
    implements Entity, Auditable, Serializable {

  private static final long serialVersionUID = -7354230469466720745L;

  @IndexField
  private String id;

  @IndexField
  private User user;

  @IndexField
  private List<Level> levels;

  @IndexField
  private List<Subject> subjects;

  @IndexField
  private List<Course> courses;

  @IndexField
  private List<Group> groups;

  @IndexField
  private User createdBy;

  @IndexField
  private LocalDateTime createdDateTime;

  @IndexField
  private User modifiedBy;

  @IndexField
  private LocalDateTime modifiedDateTime;

  UserEnrollment() {

  }

  UserEnrollment(
      String id,
      User user,
      List<Level> levels,
      List<Subject> subjects,
      List<Course> courses,
      List<Group> groups,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.user = user;
    this.levels = levels;
    this.subjects = subjects;
    this.courses = courses;
    this.groups = groups;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private User user;

    private List<Level> levels;
    private List<Subject> subjects;
    private List<Course> courses;
    private List<Group> groups;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, User user) {
      this.id = id;
      this.user = user;
    }

    public Builder levels(List<Level> levels) {
      this.levels = levels;
      return this;
    }

    public Builder subjects(List<Subject> subjects) {
      this.subjects = subjects;
      return this;
    }

    public Builder courses(List<Course> courses) {
      this.courses = courses;
      return this;
    }

    public Builder groups(List<Group> groups) {
      this.groups = groups;
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

    public UserEnrollment build() {
      return new UserEnrollment(
          this.id,
          this.user,
          this.levels,
          this.subjects,
          this.courses,
          this.groups,
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

  public Enumeration<Course> getCourses() {
    return CollectionUtils.isEmpty(courses)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(courses);
  }

  public Enumeration<Group> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(groups);
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

  @Override
  public String toString() {
    return String.format(
        "UserEnrollment{id=%s, user=%s, levels=%s, subjects=%s, courses=%s, "
            + "groups=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, user, StringUtils.join(levels, ","),
        StringUtils.join(subjects, ","), StringUtils.join(courses, ","),
        StringUtils.join(groups, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}