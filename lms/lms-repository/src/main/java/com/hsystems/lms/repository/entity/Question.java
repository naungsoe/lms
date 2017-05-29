package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(name = "questions")
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
  private List<Question> questions;

  Question() {

  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
      List<Question> questions) {

    this.id = id;
    this.type = type;
    this.body = body;
    this.hint = hint;
    this.explanation = explanation;
    this.options = options;
    this.questions = questions;
  }

  public Question(
      String id,
      QuestionType type,
      String body,
      String hint,
      String explanation,
      List<QuestionOption> options,
      List<Question> questions,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
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
    this.questions = questions;
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

  public List<QuestionOption> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyList()
        : Collections.unmodifiableList(options);
  }

  public List<Question> getQuestions() {
    return CollectionUtils.isEmpty(questions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(questions);
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
        "Question{id=%s, type=%s, body=%s, hint=%s, explanation=%s, "
            + "options=%s, questions=%s, school=%s, levels=%s, subjects=%s, "
            + "keywords=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, type, body, hint, explanation, StringUtils.join(options, ","),
        StringUtils.join(questions, ","), school,
        StringUtils.join(levels, ","), StringUtils.join(subjects, ","),
        StringUtils.join(keywords, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
