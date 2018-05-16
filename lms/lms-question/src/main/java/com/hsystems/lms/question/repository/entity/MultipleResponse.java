package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class MultipleResponse
    extends Question implements Serializable {

  private static final long serialVersionUID = -8713736815501698044L;

  private List<ChoiceOption> options;

  MultipleResponse() {

  }

  public MultipleResponse(
      String body,
      String hint,
      String explanation,
      List<ChoiceOption> options) {

    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
  }

  public Enumeration<ChoiceOption> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(options);
  }

  public void addOption(ChoiceOption... options) {
    if (CollectionUtils.isEmpty(this.options)) {
      this.options = new ArrayList<>();
    }

    Arrays.stream(options).forEach(this.options::add);
  }

  public void removeOption(ChoiceOption option) {
    this.options.remove(option);
  }
}