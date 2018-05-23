package com.hsystems.lms.school.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class School implements Entity, Serializable {

  private static final long serialVersionUID = -2374952687893081235L;

  private String id;

  private String name;

  private List<String> permissions;

  private Preferences preferences;

  School() {

  }

  School(
      String id,
      String name,
      List<String> permissions,
      Preferences preferences) {

    this.id = id;
    this.name = name;
    this.permissions = permissions;
    this.preferences = preferences;

  }

  public static class Builder {

    private String id;
    private String name;

    private List<String> permissions;
    private Preferences preferences;

    public Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder permissions(List<String> permissions) {
      this.permissions = permissions;
      return this;
    }

    public Builder preferences(Preferences preferences) {
      this.preferences = preferences;
      return this;
    }

    public School build() {
      return new School(
          this.id,
          this.name,
          this.permissions,
          this.preferences
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

  public Enumeration<String> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public Preferences getPreferences() {
    return preferences;
  }
}