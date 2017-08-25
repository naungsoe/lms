package com.hsystems.lms.repository.entity.question;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradingStrategy {

  long gradeAttempt(QuestionComponentAttempt attempt);
}