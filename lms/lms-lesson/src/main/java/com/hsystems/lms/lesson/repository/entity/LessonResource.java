package com.hsystems.lms.lesson.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.resource.Permission;
import com.hsystems.lms.resource.Resource;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.subject.repository.entity.Subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class LessonResource implements Resource, Entity, Serializable {

  private static final long serialVersionUID = -8410720229974873263L;

  private String id;

  private Lesson lesson;

  private School school;

  private List<Level> levels;

  private List<Subject> subjects;

  private List<String> keywords;

  private List<Permission> permissions;

  LessonResource() {

  }

  LessonResource(
      String id,
      Lesson lesson,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      List<Permission> permissions) {

    this.id = id;
    this.lesson = lesson;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.permissions = permissions;
  }

  public static class Builder {

    private String id;
    private Lesson lesson;

    private School school;
    private List<Level> levels;
    private List<Subject> subjects;
    private List<String> keywords;
    private List<Permission> permissions;

    public Builder(String id, Lesson lesson) {
      this.id = id;
      this.lesson = lesson;
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

    public LessonResource build() {
      return new LessonResource(
          this.id,
          this.lesson,
          this.school,
          this.levels,
          this.subjects,
          this.keywords,
          this.permissions
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

    for (Level level : levels) {
      this.levels.add(level);
    }
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

    for (Subject subject : subjects) {
      this.subjects.add(subject);
    }
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

    for (String keyword : keywords) {
      this.keywords.add(keyword);
    }
  }

  public Enumeration<Permission> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(permissions);
  }

  public void addPermission(Permission... permissions) {
    if (CollectionUtils.isEmpty(this.permissions)) {
      this.permissions = new ArrayList<>();
    }

    for (Permission permission : permissions) {
      this.permissions.add(permission);
    }
  }
}