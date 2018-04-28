package com.hsystems.lms.subject.repository.entity;

import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 1/11/16.
 */
public final class Subject implements Serializable {

  private static final long serialVersionUID = 4567554544620141045L;

  private String id;

  private String name;

  private School school;

  private User createdBy;

  private LocalDateTime createdDateTime;

  private User modifiedBy;

  private LocalDateTime modifiedDateTime;

  Subject() {

  }

  Subject(
      String id,
      String name,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.school = school;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private String name;

    private School school;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder school(School school) {
      this.school = school;
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

    public Subject build() {
      return new Subject(
          this.id,
          this.name,
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