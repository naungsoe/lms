package com.hsystems.lms.repository.entity;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradableComponentAttempt<T extends GradingStrategy>
    extends ComponentAttempt {

  void gradeAttempt(T strategy);
}