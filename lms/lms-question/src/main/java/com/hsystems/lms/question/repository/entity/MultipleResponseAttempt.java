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
    implements QuestionAttempt<MultipleResponse>, Serializable {

  private static final long serialVersionUID = 1871384672813771672L;

  private MultipleResponse multipleResponse;

  private List<ChoiceOptionAttempt> optionAttempts;

  MultipleResponseAttempt() {

  }

  public MultipleResponseAttempt(
      MultipleResponse multipleResponse,
      List<ChoiceOptionAttempt> optionAttempts) {

    this.multipleResponse = multipleResponse;
    this.optionAttempts = optionAttempts;
  }

  @Override
  public MultipleResponse getQuestion() {
    return multipleResponse;
  }

  public Enumeration<ChoiceOptionAttempt> getOptionAttempts() {
    return CollectionUtils.isEmpty(optionAttempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(optionAttempts);
  }
}