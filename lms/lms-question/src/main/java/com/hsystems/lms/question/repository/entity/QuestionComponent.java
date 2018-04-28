package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.component.GradableComponent;

/**
 * Created by naungsoe on 7/10/16.
 */
public interface QuestionComponent<T extends Question>
    extends GradableComponent<QuestionGradingStrategy> {

  T getQuestion();

  long getScore();

  QuestionGradingStrategy getGradingStrategy();
}