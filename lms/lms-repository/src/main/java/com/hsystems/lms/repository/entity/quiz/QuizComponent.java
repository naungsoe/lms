package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class QuizComponent
    implements GradableComponent<QuizGradingStrategy>, Serializable {

  private static final long serialVersionUID = 8011199044130249100L;

  @IndexField
  private String id;

  @IndexField
  private Quiz quiz;

  @IndexField
  private int order;

  @IndexField
  private String quizId;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      Quiz quiz,
      int order,
      String quizId) {

    this.id = id;
    this.quiz = quiz;
    this.order = order;
    this.quizId = quizId;
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
  public QuizGradingStrategy getGradingStrategy() {
    return new QuizGradingStrategy(this);
  }

  public String getQuizId() {
    return quizId;
  }
}
