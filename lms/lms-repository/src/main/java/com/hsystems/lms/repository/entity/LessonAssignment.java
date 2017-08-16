package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "assignments")
public class LessonAssignment
    extends ResourceAssignment implements Serializable {

  private static final long serialVersionUID = 7882721274226435888L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  LessonAssignment() {

  }

  public LessonAssignment(
      String id,
      Lesson lesson,
      List<Group> groups,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      LocalDateTime releaseDateTime,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.lesson = lesson;
    this.groups = groups;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.releaseDateTime = releaseDateTime;
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

    LessonAssignment quizAssignment = (LessonAssignment) obj;
    return id.equals(quizAssignment.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "LessonAssignment{id=%s, lesson=%s, groups=%s, startDateTime=%s, "
            + "endDateTime=%s, releaseDateTime=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, lesson, StringUtils.join(groups, ","), startDateTime,
        endDateTime, releaseDateTime, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
