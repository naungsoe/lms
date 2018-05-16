package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public final class MultipleResponseAttempt
    extends QuestionAttempt<MultipleResponse> implements Serializable {

  private static final long serialVersionUID = 6449065661561560430L;

  private List<ChoiceOptionAttempt> optionAttempts;

  MultipleResponseAttempt() {

  }

  public MultipleResponseAttempt(
      MultipleResponse question,
      List<ChoiceOptionAttempt> optionAttempts) {

    this.question = question;
    this.optionAttempts = optionAttempts;
  }

  public Enumeration<ChoiceOptionAttempt> getOptionAttempts() {
    return CollectionUtils.isEmpty(optionAttempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(optionAttempts);
  }
}