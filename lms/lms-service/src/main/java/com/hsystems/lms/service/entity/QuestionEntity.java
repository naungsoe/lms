package com.hsystems.lms.service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by administrator on 3/11/16.
 */
public class QuestionEntity implements Serializable {

  private static final long serialVersionUID = 553257369714695546L;

  private String id;

  private String type;

  private String body;

  private List<QuestionOptionEntity> options;

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

  public QuestionEntity() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public List<QuestionOptionEntity> getOptions() {
    return options;
  }

  public void setOptions(
      List<QuestionOptionEntity> options) {
    this.options = options;
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

  private void setCreatedByLastName(String createdByLastName) {
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
