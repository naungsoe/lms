package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.repository.entity.GradingStrategy;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuizGradingStrategy
    implements GradingStrategy<QuizComponentAttempt> {

  private QuizComponent component;

  QuizGradingStrategy() {

  }

  public QuizGradingStrategy(QuizComponent component) {
    this.component = component;
  }

  @Override
  public void gradeAttempt(QuizComponentAttempt componentAttempt) {

  }
}