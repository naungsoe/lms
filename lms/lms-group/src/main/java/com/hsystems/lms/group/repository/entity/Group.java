package com.hsystems.lms.group.repository.entity;

import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.school.repository.entity.School;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Group implements Entity, Serializable {

  private static final long serialVersionUID = -4257416662280706843L;

  private String id;

  private String name;

  private School school;

  Group() {

  }

  Group(
      String id,
      String name,
      School school) {

    this.id = id;
    this.name = name;
    this.school = school;
  }

  public static class Builder {

    private String id;
    private String name;

    private School school;

    public Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Group build() {
      return new Group(
          this.id,
          this.name,
          this.school
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
}