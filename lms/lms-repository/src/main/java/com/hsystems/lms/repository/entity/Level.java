package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 1/11/16.
 */
@IndexCollection(name = "levels")
public class Level extends Auditable implements Serializable {

  private static final long serialVersionUID = 1219434630963071408L;

  @IndexField
  private String id;

  @IndexField
  private String name;

  @IndexField
  private School school;

  Level() {

  }

  public Level(
      String id,
      String name) {

    this.id = id;
    this.name = name;
  }

  public Level(
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

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public School getSchool() {
    return school;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Level level = (Level) obj;
    return id.equals(level.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Level{id=%s, name=%s, school=%s, "
            + "createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, name, school, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
