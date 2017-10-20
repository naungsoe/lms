package com.hsystems.lms.repository.entity.question;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface QuestionGradingStrategy<T extends QuestionAttempt> {

  void gradeAttempt(T attempt, long maxScore);

  long getScore();
}