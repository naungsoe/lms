package com.hsystems.lms.repository.entity.question;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public class CompositeQuestionStrategy implements GradingStrategy {

  public CompositeQuestionStrategy() {

  }

  public long gradeAttempt(QuestionComponentAttempt attempt) {
    CompositeQuestionAttempt compositeAttempt
        = (CompositeQuestionAttempt) attempt;
    Enumeration<QuestionComponentAttempt> enumeration
        = compositeAttempt.getAttempts();
    long score = 0;

    while (enumeration.hasMoreElements()) {
      QuestionComponentAttempt questionAttempt = enumeration.nextElement();
      questionAttempt.gradeAttempt();
      score += questionAttempt.getScore();
    }

    return score;
  }
}