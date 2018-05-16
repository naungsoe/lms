package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.component.GradingStrategy;

public interface QuestionGradingStrategy<T extends Question>
    extends GradingStrategy<QuestionComponentAttempt<T>> {

}