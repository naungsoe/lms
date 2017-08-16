package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public class LessonSectionComponent
    extends SectionComponent implements Serializable {

  private static final long serialVersionUID = 8146307912052168618L;

  LessonSectionComponent() {

  }

  public LessonSectionComponent(
      String id,
      String title,
      String instructions,
      int order,
      List<Component> components) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.order = order;
    this.components = components;
  }

  @Override
  public void addComponent(Component... components) {
    if (CollectionUtils.isEmpty(this.components)) {
      this.components = new ArrayList<>();
    }

    Arrays.stream(components).forEach(component -> {
      checkAllowedComponent(component);
      this.components.add(component);
    });
  }

  private void checkAllowedComponent(Component component) {
    boolean isQuizComponent = component instanceof QuizComponent;
    boolean isQuestionComponent = component instanceof QuestionComponent;
    boolean isFileComponent = component instanceof FileComponent;
    boolean legalComponent = isQuizComponent
        || isQuestionComponent || isFileComponent;
    CommonUtils.checkArgument(legalComponent,
        "component is not quiz or question or file");
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    SectionComponent sectionComponent = (SectionComponent) obj;
    return id.equals(sectionComponent.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "QuizSectionComponent{id=%s, title=s%, "
            + "instructions=%s, order=%s, components=%s}",
        id, title, instructions, order,
        StringUtils.join(components, ","));
  }
}
