package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class CompositeQuestionAttempt
    implements QuestionAttempt<CompositeQuestion>, Serializable {

  private static final long serialVersionUID = 7462327592007948282L;

  private CompositeQuestion compositeQuestion;

  private List<QuestionComponentAttempt> componentAttempts;

  CompositeQuestionAttempt() {

  }

  public CompositeQuestionAttempt(
      CompositeQuestion compositeQuestion,
      List<QuestionComponentAttempt> componentAttempts) {

    this.compositeQuestion = compositeQuestion;
    this.componentAttempts = componentAttempts;
  }

  @Override
  public CompositeQuestion getQuestion() {
    return compositeQuestion;
  }

  public Enumeration<QuestionComponentAttempt> getComponentAttempts() {
    return CollectionUtils.isEmpty(componentAttempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(componentAttempts);
  }
}