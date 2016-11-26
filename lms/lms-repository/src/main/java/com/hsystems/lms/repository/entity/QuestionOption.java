package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 1/11/16.
 */
public class QuestionOption implements Serializable {

  private static final long serialVersionUID = -4444753561145528740L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private String body;

  @IndexField(type = IndexFieldType.STRING)
  private String feedback;

  @IndexField(type = IndexFieldType.BOOLEAN)
  private boolean correct;

  QuestionOption() {

  }

  public QuestionOption(
      String id,
      String body,
      String feedback,
      boolean correct) {

    this.id = id;
    this.body = body;
    this.feedback = feedback;
    this.correct = correct;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
