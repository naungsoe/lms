package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.ComponentAttempt;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class QuizAttempt implements Serializable {

  private static final long serialVersionUID = -178210646418787478L;

  @IndexField
  private List<ComponentAttempt> attempts;

  QuizAttempt() {

  }

  public QuizAttempt(List<ComponentAttempt> attempts) {
    this.attempts = attempts;
  }

  public Enumeration<ComponentAttempt> getAttempts() {
    return CollectionUtils.isEmpty(attempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(attempts);
  }
}