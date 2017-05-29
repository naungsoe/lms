package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
@IndexCollection(name = "quizzes")
public class Quiz extends Resource implements Serializable {

  private static final long serialVersionUID = 645532833693995164L;

  @IndexField
  private String id;

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

  @Override
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getInstructions() {
    return instructions;
  }

  public List<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList()
        : Collections.unmodifiableList(components);
  }

  public void addComponent(Component... component) {
    if (CollectionUtils.isEmpty(components)) {
      components = new ArrayList<>();
    }

    components.addAll(Arrays.asList(component));
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
        id, title, instructions, StringUtils.join(components, ","), school,
        StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
