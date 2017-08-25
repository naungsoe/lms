package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.ResourceAttempt;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.assignment.QuizAssignment;
import com.hsystems.lms.repository.entity.question.QuestionComponentAttempt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public class QuizAttempt extends ResourceAttempt implements Serializable {

  private static final long serialVersionUID = -8121042128361833900L;

  @IndexField
  private Quiz quiz;

  @IndexField
  private List<QuestionComponentAttempt> attempts;

  QuizAttempt() {

  }

  public QuizAttempt(
      String id,
      Quiz quiz,
      User attemptedBy,
      LocalDateTime attemptedDateTime,
      List<QuestionComponentAttempt> attempts) {

    this.id = id;
    this.quiz = quiz;
    this.attemptedBy = attemptedBy;
    this.attemptedDateTime = attemptedDateTime;
    this.attempts = attempts;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public Enumeration<QuestionComponentAttempt> getAttempts() {
    return CollectionUtils.isEmpty(attempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(attempts);
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

    QuizAssignment quizAssignment = (QuizAssignment) obj;
    return id.equals(quizAssignment.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "QuizAttempt{id=%s, quiz=%s, attemptedBy=%s, "
            + "attemptedDateTime=%s, attempts=%s}",
        id, quiz, attemptedBy, attemptedDateTime,
        StringUtils.join(attempts, ","));
  }
}
