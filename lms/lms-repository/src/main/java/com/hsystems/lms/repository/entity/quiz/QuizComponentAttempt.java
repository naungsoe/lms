package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponentAttempt;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuizComponentAttempt
    implements GradableComponentAttempt, Serializable {

  private static final long serialVersionUID = -2773908334045867221L;

  @IndexField
  private String id;

  @IndexField
  private QuizAttempt attempt;

  @IndexField
  private LocalDateTime attemptedDateTime;

  QuizComponentAttempt() {

  }

  public QuizComponentAttempt(
      String id,
      QuizAttempt attempt,
      LocalDateTime attemptedDateTime) {

    this.id = id;
    this.attempt = attempt;
    this.attemptedDateTime = attemptedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public QuizAttempt getAttempt() {
    return attempt;
  }

  @Override
  public LocalDateTime getAttemptedDateTime() {
    return attemptedDateTime;
  }
}
