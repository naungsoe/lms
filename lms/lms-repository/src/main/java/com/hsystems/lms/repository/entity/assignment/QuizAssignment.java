package com.hsystems.lms.repository.entity.assignment;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.quiz.Quiz;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public class QuizAssignment extends Assignment implements Serializable {

  private static final long serialVersionUID = 847987010896164170L;

  @IndexField
  private String id;

  @IndexField
  private Quiz quiz;

  @IndexField
  private long durationAllowed;

  @IndexField
  private boolean componentsShuffled;

  @IndexField
  private int attemptsAllowed;

  QuizAssignment() {

  }

  public QuizAssignment(
      String id,
      Quiz quiz,
      List<Group> groups,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      LocalDateTime releaseDateTime,
      long durationAllowed,
      boolean componentsShuffled,
      int attemptsAllowed,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.quiz = quiz;
    this.groups = groups;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.releaseDateTime = releaseDateTime;
    this.durationAllowed = durationAllowed;
    this.componentsShuffled = componentsShuffled;
    this.attemptsAllowed = attemptsAllowed;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public long getDurationAllowed() {
    return durationAllowed;
  }

  public boolean isComponentsShuffled() {
    return componentsShuffled;
  }

  public int getAttemptsAllowed() {
    return attemptsAllowed;
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

    QuizAssignment assignment = (QuizAssignment) obj;
    return id.equals(assignment.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "QuizAssignment{id=%s, quiz=%s, groups=%s, startDateTime=%s, "
            + "endDateTime=%s, releaseDateTime=%s, durationAllowed=%s, "
            + "componentsShuffled=%s, attemptsAllowed=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, quiz, StringUtils.join(groups, ","), startDateTime, endDateTime,
        releaseDateTime, durationAllowed, componentsShuffled, attemptsAllowed,
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}