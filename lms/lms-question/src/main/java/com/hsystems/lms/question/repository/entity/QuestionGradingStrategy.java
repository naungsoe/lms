package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.component.GradingStrategy;

/**
 * Created by naungsoe on 19/12/16.
 */
public interface QuestionGradingStrategy<T extends QuestionComponentAttempt>
    extends GradingStrategy<T> {

  long calculateScore(T componentAttempt);
}