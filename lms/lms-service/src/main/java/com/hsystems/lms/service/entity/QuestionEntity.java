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

  private List<QuestionOptionEntity> optionEntities;

  private String schoolId;

  private String schoolName;

  private String createdById;

  private String createdByName;

  private String createdDateTime;

  private String modifiedById;

  private String modifiedByName;

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

  public List<QuestionOptionEntity> getOptionEntities() {
    return optionEntities;
  }

  public void setOptionEntities(
      List<QuestionOptionEntity> optionEntities) {
    this.optionEntities = optionEntities;
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

  public String getCreatedByName() {
    return createdByName;
  }

  public void setCreatedByName(String createdByName) {
    this.createdByName = createdByName;
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

  public String getModifiedByName() {
    return modifiedByName;
  }

  public void setModifiedByName(String modifiedByName) {
    this.modifiedByName = modifiedByName;
  }

  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }
}
