package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.ListUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(name = "questions")
public class CompositeQuestion extends Question {

  private static final long serialVersionUID = -2938792468928982754L;

  @IndexField(type = IndexFieldType.LIST)
  private List<Question> questions;

  CompositeQuestion() {

  }

  public CompositeQuestion(
      String id,
      String body,
      String hint,
      String explanation,
      List<Question> questions) {

    super(id, QuestionType.COMPOSITE, body, hint,
        explanation, Collections.emptyList());
    this.questions = questions;
  }

  public CompositeQuestion(
      String id,
      String body,
      String hint,
      String explanation,
      List<Question> questions,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    super(id, QuestionType.COMPOSITE, body, hint, explanation,
        Collections.emptyList(), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
    this.questions = questions;
  }

  public List<Question> getQuestions() {
    return ListUtils.isEmpty(questions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(questions);
  }

  @Override
  public String toString() {
    return String.format(
        "Question{id=%s, type=%s, body=%s, hint=%s, explanation=%s, "
            + "questions=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        getId(), getType(), getBody(), getHint(), getExplanation(),
        StringUtils.join(questions, ","), createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
