package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestion;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestionComponent;

public class UnknownQuestionComponentBean
    implements QuestionComponentBean<UnknownQuestion> {

  public UnknownQuestionComponentBean() {

  }

  @Override
  public String getId() {
    return "";
  }

  @Override
  public UnknownQuestion getQuestion() {
    return new UnknownQuestion();
  }

  @Override
  public long getScore() {
    return 0;
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public String getResourceId() {
    return "";
  }

  @Override
  public String getParentId() {
    return "";
  }

  @Override
  public QuestionComponent<UnknownQuestion> getQuestionComponent() {
    return new UnknownQuestionComponent();
  }
}