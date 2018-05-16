package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.level.service.model.LevelModel;
import com.hsystems.lms.school.service.model.SchoolModel;
import com.hsystems.lms.school.service.model.UserModel;
import com.hsystems.lms.subject.service.model.SubjectModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResourceModel<T extends QuestionModel>
    implements Serializable {

  private static final long serialVersionUID = -8477397666254729692L;

  private String id;

  private T question;

  private SchoolModel school;

  private List<LevelModel> levels;

  private List<SubjectModel> subjects;

  private List<String> keywords;

  private List<PermissionModel> permissions;

  private UserModel createdBy;

  private String createdOn;

  private UserModel modifiedBy;

  private String modifiedOn;

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

  public UserModel getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserModel createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public UserModel getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(UserModel modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(String modifiedOn) {
    this.modifiedOn = modifiedOn;
  }
}