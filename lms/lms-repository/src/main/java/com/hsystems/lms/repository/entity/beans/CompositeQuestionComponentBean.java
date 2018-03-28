package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;

public class CompositeQuestionComponentBean
    implements QuestionComponentBean<CompositeQuestion>, Serializable {

  private static final long serialVersionUID = -6837544360765370750L;

  @IndexField
  private String id;

  @IndexField
  private CompositeQuestion question;

  @IndexField
  private long score;

  @IndexField
  private int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  CompositeQuestionComponentBean() {

  }

  public CompositeQuestionComponentBean(
      QuestionComponent<CompositeQuestion> component,
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
  public CompositeQuestion getQuestion() {
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
  public QuestionComponent<CompositeQuestion> getQuestionComponent() {
    return new CompositeQuestionComponent(id, question, score, order);
  }
}