package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class CompositeQuestionAttempt implements QuestionAttempt {

  private static final long serialVersionUID = 5365281305612328449L;

  @IndexField
  private List<QuestionComponentAttempt> attempts;

  CompositeQuestionAttempt() {

  }

  public CompositeQuestionAttempt(
      List<QuestionComponentAttempt> attempts) {

    this.attempts = attempts;
  }

  public Enumeration<QuestionComponentAttempt> getAttempts() {
    return CollectionUtils.isEmpty(attempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(attempts);
  }
}
