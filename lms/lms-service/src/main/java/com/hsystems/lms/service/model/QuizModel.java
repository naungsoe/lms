package com.hsystems.lms.service.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class QuizModel implements Serializable {

  private static final long serialVersionUID = -1599914890983126737L;

  private String id;

  private String title;

  private String instructions;

  private List<QuizSectionModel> sections;

  private String schoolId;

  private String schoolName;

  private String createdById;

  private String createdByFirstName;

  private String createdByLastName;

  private String createdDateTime;

  private String modifiedById;

  private String modifiedByFirstName;

  private String modifiedByLastName;

  private String modifiedDateTime;

  public QuizModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public List<QuizSectionModel> getSections() {
    return Collections.unmodifiableList(sections);
  }

  public void setSections(
      List<QuizSectionModel> sections) {
    this.sections = sections;
  }

  public String getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(String schoolId) {
    this.schoolId = schoolId;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(String createdById) {
    this.createdById = createdById;
  }

  public String getCreatedByFirstName() {
    return createdByFirstName;
  }

  public void setCreatedByFirstName(String createdByFirstName) {
    this.createdByFirstName = createdByFirstName;
  }

  public String getCreatedByLastName() {
    return createdByLastName;
  }

  public void setCreatedByLastName(String createdByLastName) {
    this.createdByLastName = createdByLastName;
  }

  public String getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public String getModifiedById() {
    return modifiedById;
  }

  public void setModifiedById(String modifiedById) {
    this.modifiedById = modifiedById;
  }

  public String getModifiedByFirstName() {
    return modifiedByFirstName;
  }

  public void setModifiedByFirstName(String modifiedByFirstName) {
    this.modifiedByFirstName = modifiedByFirstName;
  }

  public String getModifiedByLastName() {
    return modifiedByLastName;
  }

  public void setModifiedByLastName(String modifiedByLastName) {
    this.modifiedByLastName = modifiedByLastName;
  }

  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }
}
