package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.repository.entity.GradingStrategy;

/**
 * Created by naungsoe on 19/12/16.
 */
public interface QuestionGradingStrategy<T extends QuestionComponentAttempt>
    extends GradingStrategy<T> {

  long calculateScore(T componentAttempt);
}