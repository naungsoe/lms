package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.quiz.Quiz;
import com.hsystems.lms.repository.entity.quiz.QuizComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuizComponentBean implements ComponentBean<QuizComponent> {

  private static final long serialVersionUID = -1902364835889721439L;

  @IndexField
  private String id;

  @IndexField
  private Quiz quiz;

  @IndexField
  private int order;

  @IndexField
  private String quizId;

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  QuizComponentBean() {

  }

  public QuizComponentBean(
      QuizComponent component,
      String resourceId,
      String parentId) {

    this.id = component.getId();
    this.quiz = component.getQuiz();
    this.order = component.getOrder();
    this.quizId = component.getQuizId();
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return id;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  @Override
  public int getOrder() {
    return order;
  }

  public String getQuizId() {
    return quizId;
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
  public QuizComponent getComponent() {
    return new QuizComponent(id, quiz, order, quizId);
  }
}
