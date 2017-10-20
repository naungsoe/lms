package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.service.model.course.CourseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UserEnrollmentModel extends AuditableModel
    implements Serializable {

  private static final long serialVersionUID = -983251016138782310L;

  private String id;

  private UserModel user;

  private List<LevelModel> levels;

  private List<SubjectModel> subjects;

  private List<CourseModel> courses;

  private List<GroupModel> groups;

  public UserEnrollmentModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public List<LevelModel> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyList() : levels;
  }

  public void setLevels(List<LevelModel> levels) {
    this.levels = new ArrayList<>();
    this.levels.addAll(levels);
  }

  public List<SubjectModel> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyList() : subjects;
  }

  public void setSubjects(List<SubjectModel> subjects) {
    this.subjects = new ArrayList<>();
    this.subjects.addAll(subjects);
  }

  public List<CourseModel> getCourses() {
    return CollectionUtils.isEmpty(courses)
        ? Collections.emptyList() : courses;
  }

  public void setCourses(List<CourseModel> courses) {
    this.courses = new ArrayList<>();
    this.courses.addAll(courses);
  }

  public List<GroupModel> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyList() : groups;
  }

  public void setGroups(List<GroupModel> groups) {
    this.groups = new ArrayList<>();
    this.groups.addAll(groups);
  }
}