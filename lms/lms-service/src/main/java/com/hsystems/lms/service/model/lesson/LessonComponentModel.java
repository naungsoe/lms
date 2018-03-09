package com.hsystems.lms.service.model.lesson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LessonComponentModel extends ComponentModel
    implements Serializable {

  private static final long serialVersionUID = -2237537672590406242L;

  private LessonModel lesson;

  private String lessonId;

  public LessonComponentModel() {

  }

  public LessonModel getLesson() {
    return lesson;
  }

  public void setLesson(LessonModel lesson) {
    this.lesson = lesson;
  }

  public String getLessonId() {
    return lessonId;
  }

  public void setLessonId(String lessonId) {
    this.lessonId = lessonId;
  }
}