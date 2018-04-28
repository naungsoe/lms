package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;
import com.hsystems.lms.common.annotation.IndexField;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 1/11/16.
 */
@IndexDocument(namespace = "lms", collection = "subjects")
public final class Subject implements Entity, SchoolScoped, Auditable {

  private static final long serialVersionUID = 7171159112804915595L;

  @IndexField
  private String id;

  @IndexField
  private String name;

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

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
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

  @Override
  public String toString() {
    return String.format(
        "Subject{id=%s, name=%s, school=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, name, school, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
