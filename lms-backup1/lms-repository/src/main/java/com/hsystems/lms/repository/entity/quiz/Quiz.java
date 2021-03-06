package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class Quiz implements Serializable {

  private static final long serialVersionUID = -332449270039972756L;

  @IndexField
  private String title;

  @IndexField
  private String description;

  protected List<Component> components;

  Quiz() {

  }

  public Quiz(
      String title,
      String description,
      List<Component> components) {

    this.title = title;
    this.description = description;
    this.components = components;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
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

    QuizCompositionStrategy compositionStrategy
        = new QuizCompositionStrategy();
    Arrays.stream(components).forEach(component -> {
      compositionStrategy.validate(component);
      this.components.add(component);
    });
  }

  public void removeComponent(Component component) {
    QuizCompositionStrategy compositionStrategy
        = new QuizCompositionStrategy();
    compositionStrategy.validate(component);
    this.components.remove(component);
  }
}