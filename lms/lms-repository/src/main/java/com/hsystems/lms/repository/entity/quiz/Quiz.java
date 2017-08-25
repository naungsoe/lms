package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
@IndexCollection(namespace = "lms", name = "quizzes")
public class Quiz extends Resource
    implements Serializable {

  private static final long serialVersionUID = 140365368286164278L;

  @IndexField
  private String title;

  @IndexField
  private String instructions;

  @IndexField
  protected List<Component> components;

  Quiz() {

  }

  public Quiz(
      String id,
      String title,
      String instructions,
      List<Component> components) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.components = components;
  }

  public Quiz(
      String id,
      String title,
      String instructions,
      List<Component> components,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.title = title;
    this.instructions = instructions;
    this.components = components;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public String getTitle() {
    return title;
  }

  public String getInstructions() {
    return instructions;
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

    Arrays.stream(components).forEach(component -> {
      checkSectionComponent(component);
      this.components.add(component);
    });
  }

  private void checkSectionComponent(Component component) {
    boolean isSectionComponent = component instanceof SectionComponent;
    CommonUtils.checkArgument(isSectionComponent, "component is not section");

    SectionComponent sectionComponent = (SectionComponent) component;
    Enumeration<Component> enumeration = sectionComponent.getComponents();

    while (enumeration.hasMoreElements()) {
      Component element = enumeration.nextElement();
      checkQuestionComponent(element);
    }
  }

  private void checkQuestionComponent(Component component) {
    boolean isQuestionComponent = component instanceof QuestionComponent;
    CommonUtils.checkArgument(isQuestionComponent, "component is not question");
  }

  public void removeComponent(Component component) {
    checkSectionComponent(component);
    this.components.remove(component);
  }

  public long getScore() {
    long score = 0;

    for (Component component : components) {
      SectionComponent sectionComponent = (SectionComponent) component;
      Enumeration<Component> enumeration = sectionComponent.getComponents();

      while (enumeration.hasMoreElements()) {
        Component element = enumeration.nextElement();
        QuestionComponent questionComponent = (QuestionComponent) element;
        score += questionComponent.getScore();
      }
    }

    return score;
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

    Quiz quiz = (Quiz) obj;
    return id.equals(quiz.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Quiz{id=%s, title=%s, instructions=%s, components=%s, school=%s, "
            + "levels=%s, subjects=%s, keywords=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, title, instructions, StringUtils.join(components, ","),
        school, StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
