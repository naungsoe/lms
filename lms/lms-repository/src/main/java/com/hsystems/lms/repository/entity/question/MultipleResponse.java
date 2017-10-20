package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
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
public final class MultipleResponse implements Question, Serializable {

  private static final long serialVersionUID = -1138463617202472009L;

  @IndexField
  protected String body;

  @IndexField
  protected String hint;

  @IndexField
  protected String explanation;

  @IndexField
  protected List<ChoiceOption> options;

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

  @Override
  public QuestionGradingStrategy getGradingStrategy() {
    return new MultipleResponseGradingStrategy(this);
  }
}