package com.hsystems.lms.repository.entity;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradableComponentAttempt extends ComponentAttempt {

  void gradeAttempt();

  long getScore();
}