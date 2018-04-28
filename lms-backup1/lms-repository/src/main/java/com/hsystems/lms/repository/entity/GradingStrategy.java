package com.hsystems.lms.repository.entity;

public interface GradingStrategy<T extends GradableComponentAttempt> {

  void gradeAttempt(T componentAttempt);
}