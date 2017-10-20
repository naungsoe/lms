package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleResponseAttempt
    implements QuestionAttempt, Serializable {

  private static final long serialVersionUID = 245974893854136436L;

  @IndexField
  private List<ChoiceOptionAttempt> attempts;

  MultipleResponseAttempt() {

  }

  public MultipleResponseAttempt(
      List<ChoiceOptionAttempt> attempts) {

    this.attempts = attempts;
  }

  public Enumeration<ChoiceOptionAttempt> getAttempts() {
    return CollectionUtils.isEmpty(attempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(attempts);
  }
}