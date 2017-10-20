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
public final class CompositeQuestion implements Question, Serializable {

  private static final long serialVersionUID = -5053523521808585395L;

  @IndexField
  protected String body;

  @IndexField
  protected String hint;

  @IndexField
  protected String explanation;

  @IndexField
  private List<QuestionComponent> components;

  CompositeQuestion() {

  }

  public CompositeQuestion(
      String body,
      String hint,
      String explanation,
      List<QuestionComponent> components) {

    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.components = components;
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

  public Enumeration<QuestionComponent> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  public void addComponent(QuestionComponent... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    Arrays.stream(components).forEach(this.components::add);
  }

  public void removeComponent(QuestionComponent component) {
    this.components.remove(component);
  }

  @Override
  public QuestionGradingStrategy getGradingStrategy() {
    return new CompositeQuestionGradingStrategy(this);
  }
}
