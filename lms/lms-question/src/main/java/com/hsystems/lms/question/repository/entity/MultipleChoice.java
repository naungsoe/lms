package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class MultipleChoice
    extends Question implements Serializable {

  private static final long serialVersionUID = -8029767190954051125L;

  private List<ChoiceOption> options;

  MultipleChoice() {

  }

  public MultipleChoice(
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

    for (ChoiceOption option : options) {
      this.options.add(option);
    }
  }

  public void removeOption(ChoiceOption option) {
    this.options.remove(option);
  }
}