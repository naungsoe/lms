package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public class QuestionTypeModel implements Serializable {

  private static final long serialVersionUID = -7521639246581594872L;

  private String id;

  private String name;

  QuestionTypeModel() {

  }

  public QuestionTypeModel(
      String id,
      String name) {

    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
