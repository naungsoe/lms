package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public final class CompositeQuestion implements Question {

  private static final long serialVersionUID = 6296284278339657953L;

  @IndexField
  protected String body;

  @IndexField
  protected String hint;

  @IndexField
  protected String explanation;

  @IndexField
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
