package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.Builder;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Level;
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
public final class CompositeQuestion extends Question
    implements Serializable {

  private static final long serialVersionUID = -2477002946522794897L;

  @IndexField
  private List<QuestionComponent> components;

  CompositeQuestion() {

  }

  public CompositeQuestion(
      String id,
      String body,
      String hint,
      String explanation,
      List<QuestionComponent> components) {

    this.id = id;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.components = components;
  }

  public CompositeQuestion(
      String id,
      String body,
      String hint,
      String explanation,
      List<QuestionComponent> components,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
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

  public static class QuestionBuilder
      implements Builder<CompositeQuestion> {

    private String id;
    private String body;
    private String hint;
    private String explanation;
    private List<QuestionComponent> components;

    public QuestionBuilder(
        String id,
        String body,
        String hint,
        String explanation,
        List<QuestionComponent> components) {

      this.id = id;
      this.body = body;
      this.hint = hint;
      this.explanation = explanation;
      this.components = components;
    }

    public CompositeQuestion build() {
      return new CompositeQuestion(
          this.id,
          this.body,
          this.hint,
          this.explanation,
          this.components
      );
    }
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
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    CompositeQuestion question = (CompositeQuestion) obj;
    return id.equals(question.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "CompositeQuestion{id=%s, body=%s, hint=%s, explanation=%s, "
            + "components=%s, school=%s, levels=%s, subjects=%s, "
            + "keywords=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, body, hint, explanation, StringUtils.join(components, ","), school,
        StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
