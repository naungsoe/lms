package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuizComponent
    implements GradableComponent<QuizComponentAttempt>, Serializable {

  private static final long serialVersionUID = 1129182302928930252L;

  @IndexField
  private String id;

  @IndexField
  private Quiz quiz;

  @IndexField
  private int order;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      Quiz quiz,
      int order) {

    this.id = id;
    this.quiz = quiz;
    this.order = order;
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

  @Override
  public void gradeAttempt(QuizComponentAttempt attempt) {

  }
}
