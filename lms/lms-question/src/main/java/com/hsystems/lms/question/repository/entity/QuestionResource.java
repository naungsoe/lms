package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.resource.Permission;
import com.hsystems.lms.entity.resource.Resource;
import com.hsystems.lms.entity.resource.Status;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.entity.User;
import com.hsystems.lms.subject.repository.entity.Subject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class QuestionResource<T extends Question>
    implements Resource, Serializable {

  private static final long serialVersionUID = -7350094020858784927L;

  private String id;

  private T question;

  private School school;

  private List<Level> levels;

  private List<Subject> subjects;

  private List<String> keywords;

  private List<Permission> permissions;

  private Status status;

  private User createdBy;

  private LocalDateTime createdDateTime;

  private User modifiedBy;

  private LocalDateTime modifiedDateTime;

  QuestionResource() {

  }

  QuestionResource(
      String id,
      T question,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      List<Permission> permissions,
      Status status,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.question = question;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.permissions = permissions;
    this.status = status;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder<T extends Question> {

    private String id;
    private T question;

    private School school;
    private List<Level> levels;
    private List<Subject> subjects;
    private List<String> keywords;
    private List<Permission> permissions;
    private Status status;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, T question) {
      this.id = id;
      this.question = question;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Builder levels(List<Level> levels) {
      this.levels = levels;
      return this;
    }

    public Builder subjects(List<Subject> subjects) {
      this.subjects = subjects;
      return this;
    }

    public Builder keywords(List<String> keywords) {
      this.keywords = keywords;
      return this;
    }

    public Builder permissions(List<Permission> permissions) {
      this.permissions = permissions;
      return this;
    }

    public Builder status(Status status) {
      this.status = status;
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

    public QuestionResource build() {
      return new QuestionResource(
          this.id,
          this.question,
          this.school,
          this.levels,
          this.subjects,
          this.keywords,
          this.permissions,
          this.status,
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

  public T getQuestion() {
    return question;
  }

  public School getSchool() {
    return school;
  }

  public Enumeration<Level> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(levels);
  }

  public void addLevel(Level... levels) {
    if (CollectionUtils.isEmpty(this.levels)) {
      this.levels = new ArrayList<>();
    }

    Arrays.stream(levels).forEach(this.levels::add);
  }

  public Enumeration<Subject> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(subjects);
  }

  public void addSubject(Subject... subjects) {
    if (CollectionUtils.isEmpty(this.subjects)) {
      this.subjects = new ArrayList<>();
    }

    Arrays.stream(subjects).forEach(this.subjects::add);
  }

  public Enumeration<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(keywords);
  }

  public void addKeyword(String... keywords) {
    if (CollectionUtils.isEmpty(this.keywords)) {
      this.keywords = new ArrayList<>();
    }

    Arrays.stream(keywords).forEach(this.keywords::add);
  }

  @Override
  public Enumeration<Permission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  @Override
  public void addPermission(Permission... permissions) {
    if (CollectionUtils.isEmpty(this.permissions)) {
      this.permissions = new ArrayList<>();
    }

    Arrays.stream(permissions).forEach(this.permissions::add);
  }

  @Override
  public Status getStatus() {
    return status;
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