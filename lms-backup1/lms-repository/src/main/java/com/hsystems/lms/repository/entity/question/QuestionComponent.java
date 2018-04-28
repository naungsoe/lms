package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.repository.entity.GradableComponent;

/**
 * Created by naungsoe on 7/10/16.
 */
public interface QuestionComponent<T extends Question>
    extends GradableComponent<QuestionGradingStrategy> {

  T getQuestion();

  long getScore();

  QuestionGradingStrategy getGradingStrategy();
}