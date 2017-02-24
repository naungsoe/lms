package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionModel implements Serializable {

  private static final long serialVersionUID = 553257369714695546L;

  private String id;

  private String type;

  private String body;

  private String hint;

  private String explanation;

  private List<QuestionOptionModel> options;

  private String createdById;

  private String createdByFirstName;

  private String createdByLastName;

  private String createdDateTime;

  private String modifiedById;

  private String modifiedByFirstName;

  private String modifiedByLastName;

  private String modifiedDateTime;

  public QuestionModel() {

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

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public List<QuestionOptionModel> getOptions() {
    return Collections.unmodifiableList(options);
  }

  public void setOptions(
      List<QuestionOptionModel> options) {
    this.options = options;
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
