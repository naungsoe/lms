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
public final class CompositeQuestion
    extends Question implements Serializable {

  private static final long serialVersionUID = 4045707173466170266L;

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

  public Enumeration<QuestionComponent> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  public void addComponent(QuestionComponent... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    CompositeQuestionCompositionSpecification compositionSpecification
        = new CompositeQuestionCompositionSpecification();
    Arrays.stream(components).forEach(component -> {
      if (compositionSpecification.isSatisfiedBy(component)) {
        this.components.add(component);
      }
    });
  }

  public void removeComponent(QuestionComponent component) {
    CompositeQuestionCompositionSpecification compositionSpecification
        = new CompositeQuestionCompositionSpecification();

    if (compositionSpecification.isSatisfiedBy(component)) {
      this.components.remove(component);
    }
  }
}