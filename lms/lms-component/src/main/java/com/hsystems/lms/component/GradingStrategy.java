package com.hsystems.lms.component;

public interface GradingStrategy<T extends GradableComponentAttempt> {

   void gradeAttempt(T componentAttempt);
}