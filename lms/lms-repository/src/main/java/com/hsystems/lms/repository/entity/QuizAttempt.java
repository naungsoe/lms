package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
public class QuizAttempt extends ResourceAttempt implements Serializable {

  private static final long serialVersionUID = 7299021041371242304L;

  @IndexField
  private String id;

  QuizAttempt() {

  }

  public QuizAttempt(
      String id,
      User user,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.user = user;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
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
        "QuizAssignment{id=%s, user=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, user, createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
