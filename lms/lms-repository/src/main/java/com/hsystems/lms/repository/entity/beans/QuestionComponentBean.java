package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.MultipleResponseComponent;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestionComponent;

public class QuestionComponentBean<T extends Question>
    implements ComponentBean<QuestionComponent> {

  private static final long serialVersionUID = -5019701850864627482L;

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

  @Override
  public QuestionComponent getComponent() {
    if (question instanceof CompositeQuestion) {
      CompositeQuestion composite = (CompositeQuestion) question;
      return new CompositeQuestionComponent(id, composite, score, order);

    } else if (question instanceof MultipleChoice) {
      MultipleChoice multipleChoice = (MultipleChoice) question;
      return new MultipleChoiceComponent(id, multipleChoice, score, order);

    } else if (question instanceof MultipleResponse) {
      MultipleResponse multipleResponse = (MultipleResponse) question;
      return new MultipleResponseComponent(id, multipleResponse, score, order);
    }

    return new UnknownQuestionComponent();
  }
}