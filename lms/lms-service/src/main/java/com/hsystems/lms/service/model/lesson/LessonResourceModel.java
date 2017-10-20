package com.hsystems.lms.service.model.lesson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ResourceModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LessonResourceModel extends ResourceModel
    implements Serializable {

  private static final long serialVersionUID = -4965916570404695356L;

  private LessonModel lesson;

  public LessonResourceModel() {

  }

  public LessonModel getLesson() {
    return lesson;
  }

  public void setLesson(LessonModel lesson) {
    this.lesson = lesson;
  }
}