package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.MultipleResponseComponent;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;

public class MultipleResponseComponentBean
    implements QuestionComponentBean<MultipleResponse>, Serializable {

  private static final long serialVersionUID = -8956245543987871484L;

  @IndexField
  private String id;

  @IndexField
  private MultipleResponse question;

  @IndexField
  private long score;

  @IndexField
  private int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  MultipleResponseComponentBean() {

  }

  public MultipleResponseComponentBean(
      QuestionComponent<MultipleResponse> component,
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

  @Override
  public MultipleResponse getQuestion() {
    return question;
  }

  @Override
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

  @Override
  public QuestionComponent<MultipleResponse> getQuestionComponent() {
    return new MultipleResponseComponent(id, question, score, order);
  }
}