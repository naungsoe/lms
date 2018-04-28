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
public final class MultipleChoice implements Question, Serializable {

  private static final long serialVersionUID = -4546547021058878954L;

  protected String body;

  protected String hint;

  protected String explanation;

  protected List<ChoiceOption> options;

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

  @Override
  public String getBody() {
    return body;
  }

  @Override
  public String getHint() {
    return hint;
  }

  @Override
  public String getExplanation() {
    return explanation;
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