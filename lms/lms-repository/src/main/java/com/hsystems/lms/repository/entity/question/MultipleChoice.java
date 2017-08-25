package com.hsystems.lms.repository.entity.question;

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
public final class MultipleChoice extends Question
    implements Serializable {

  private static final long serialVersionUID = 9041109973626247394L;

  @IndexField
  protected List<QuestionOption> options;

  MultipleChoice() {

  }

  public MultipleChoice(
      String id,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options) {

    this.id = id;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
  }

  public MultipleChoice(
      String id,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
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
    this.options = options;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public Enumeration<QuestionOption> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(options);
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

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    MultipleChoice question = (MultipleChoice) obj;
    return id.equals(question.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "MultipleChoice{id=%s, body=%s, hint=%s, explanation=%s, options=%s, "
            + "school=%s, levels=%s, subjects=%s, keywords=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, body, hint, explanation, StringUtils.join(options, ","), school,
        StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
