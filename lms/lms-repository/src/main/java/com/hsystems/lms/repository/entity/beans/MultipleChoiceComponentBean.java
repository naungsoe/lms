package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;

public class MultipleChoiceComponentBean
    implements QuestionComponentBean<MultipleChoice>, Serializable {

  private static final long serialVersionUID = 5761527757618017534L;

  @IndexField
  private String id;

  @IndexField
  private MultipleChoice question;

  @IndexField
  private long score;

  @IndexField
  private int order;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  MultipleChoiceComponentBean() {

  }

  public MultipleChoiceComponentBean(
      QuestionComponent<MultipleChoice> component,
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
  public MultipleChoice getQuestion() {
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
  public QuestionComponent<MultipleChoice> getQuestionComponent() {
    return new MultipleChoiceComponent(id, question, score, order);
  }
}