package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.AccessControl;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(namespace = "lms", name = "questions")
public class Question extends Resource implements Serializable {

  private static final long serialVersionUID = 6004706395678775298L;

  @IndexField
  private String id;

  @IndexField
  private QuestionType type;

  @IndexField
  private String body;

  @IndexField
  private String hint;

  @IndexField
  private String explanation;

  @IndexField
  private List<QuestionOption> options;

  @IndexField
  protected List<Component> components;

  Question() {

  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
      List<Component> components) {

    this.id = id;
    this.type = type;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
    this.components = components;
  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
      List<Component> components,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      List<AccessControl> accessControls,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.type = type;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
    this.components = components;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.accessControls = accessControls;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public QuestionType getType() {
    return type;
  }

  public String getBody() {
    return body;
  }

  public String getHint() {
    return hint;
  }

  public String getExplanation() {
    return explanation;
  }

  public Enumeration<QuestionOption> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(options);
  }

  public Enumeration<Component> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(components);
  }

  public void addOption(QuestionOption... options) {
    if (CollectionUtils.isEmpty(this.options)) {
      this.options = new ArrayList<>();
    }

    Arrays.stream(options).forEach(this.options::add);
  }

  public void removeOption(QuestionOption option) {
    this.options.remove(option);
  }

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
    boolean isQuestionComponent = component instanceof QuestionComponent;
    CommonUtils.checkArgument(isQuestionComponent, "component is not question");

    QuestionComponent questionComponent = (QuestionComponent) component;
    Question question = questionComponent.getQuestion();
    boolean isCompositeQuestion = QuestionType.COMPOSITE == question.getType();
    CommonUtils.checkArgument(!isCompositeQuestion,
        "question is composite question");
  }

  public void removeComponent(Component component) {
    checkAllowedComponent(component);
    this.components.remove(component);
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

    Question question = (Question) obj;
    return id.equals(question.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "Question{id=%s, type=%s, body=%s, hint=%s, "
            + "explanation=%s, options=%s, components=%s, "
            + "school=%s, levels=%s, subjects=%s, keywords=%s, "
            + "accessControls=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, type, body, hint, explanation, StringUtils.join(options, ","),
        StringUtils.join(components, ","), school,
        StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), StringUtils.join(accessControls, ","),
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
