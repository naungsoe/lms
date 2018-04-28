package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.entity.component.Component;
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

  private static final long serialVersionUID = -4332905571773036876L;

  protected String body;

  protected String hint;

  protected String explanation;

  private List<Component> components;

  CompositeQuestion() {

  }

  public CompositeQuestion(
      String body,
      String hint,
      String explanation,
      List<Component> components) {

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

  public Enumeration<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  public void addComponent(Component... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    CompositeQuestionCompositionStrategy compositionStrategy
        = new CompositeQuestionCompositionStrategy();
    Arrays.stream(components).forEach(component -> {
      compositionStrategy.validate(component);
      this.components.add(component);
    });
  }

  public void removeComponent(Component component) {
    CompositeQuestionCompositionStrategy compositionStrategy
        = new CompositeQuestionCompositionStrategy();
    compositionStrategy.validate(component);
    this.components.remove(component);
  }
}