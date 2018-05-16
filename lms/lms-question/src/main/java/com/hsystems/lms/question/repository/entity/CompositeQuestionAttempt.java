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
    extends QuestionAttempt<CompositeQuestion> implements Serializable {

  private static final long serialVersionUID = 5961743909865439323L;

  private List<QuestionComponentAttempt> componentAttempts;

  CompositeQuestionAttempt() {

  }

  public CompositeQuestionAttempt(
      CompositeQuestion question,
      List<QuestionComponentAttempt> componentAttempts) {

    this.question = question;
    this.componentAttempts = componentAttempts;
  }

  public Enumeration<QuestionComponentAttempt> getComponentAttempts() {
    return CollectionUtils.isEmpty(componentAttempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(componentAttempts);
  }
}