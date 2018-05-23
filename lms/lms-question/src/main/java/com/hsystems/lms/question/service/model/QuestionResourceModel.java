package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.level.service.model.LevelModel;
import com.hsystems.lms.school.service.model.AuditableModel;
import com.hsystems.lms.school.service.model.SchoolModel;
import com.hsystems.lms.subject.service.model.SubjectModel;

import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResourceModel<T extends QuestionModel>
    extends AuditableModel {

  private static final long serialVersionUID = 6031399376899659160L;

  private String id;

  private T question;

  private SchoolModel school;

  private List<LevelModel> levels;

  private List<SubjectModel> subjects;

  private List<String> keywords;

  private List<PermissionModel> permissions;

  public QuestionResourceModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public T getQuestion() {
    return question;
  }

  public void setQuestion(T question) {
    this.question = question;
  }

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public List<LevelModel> getLevels() {
    return levels;
  }

  public void setLevels(
      List<LevelModel> levels) {
    this.levels = levels;
  }

  public List<SubjectModel> getSubjects() {
    return subjects;
  }

  public void setSubjects(
      List<SubjectModel> subjects) {
    this.subjects = subjects;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public List<PermissionModel> getPermissions() {
    return permissions;
  }

  public void setPermissions(
      List<PermissionModel> permissions) {
    this.permissions = permissions;
  }
}