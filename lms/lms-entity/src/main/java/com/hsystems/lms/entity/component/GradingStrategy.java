package com.hsystems.lms.entity.component;

public interface GradingStrategy<T extends GradableComponentAttempt> {

  void gradeAttempt(T componentAttempt);
}