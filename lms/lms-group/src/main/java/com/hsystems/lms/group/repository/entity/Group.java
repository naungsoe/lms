package com.hsystems.lms.group.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.school.repository.entity.School;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class Group implements Entity, Serializable {

  private static final long serialVersionUID = -136122721035736189L;

  private String id;

  private String name;

  private List<User> members;

  private School school;

  Group() {

  }

  Group(
      String id,
      String name,
      List<User> members,
      School school) {

    this.id = id;
    this.name = name;
    this.members = members;
    this.school = school;
  }

  public static class Builder {

    private String id;
    private String name;

    private List<User> members;
    private School school;

    public Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder members(List<User> members) {
      this.members = members;
      return this;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Group build() {
      return new Group(
          this.id,
          this.name,
          this.members,
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

  public Enumeration<User> getMembers() {
    return CollectionUtils.isEmpty(members)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(members);
  }

  public School getSchool() {
    return school;
  }
}