package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;

public class QuestionComponentBean<T extends Question>
    implements ComponentBean, Serializable {

  private static final long serialVersionUID = 3742622771669513564L;

  @IndexField
  private String id;

  @IndexField
  private T question;

  @IndexField
  private long score;

  @IndexField
  private int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  QuestionComponentBean() {

  }

  public QuestionComponentBean(
      QuestionComponent<T> component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.question = component.getQuestion();
    this.score = component.getScore();
    this.order = component.getOrder();
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return id;
  }

  public T getQuestion() {
    return question;
  }

  public long getScore() {
    return score;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public String getResourceId() {
    return resourceId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }
}