package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

public interface QuestionComponentBean<T extends Question>
    extends ComponentBean {

  T getQuestion();

  long getScore();

  QuestionComponent<T> getQuestionComponent();
}