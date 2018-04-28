package com.hsystems.lms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public class Question extends Auditable implements Serializable {

  private static final long serialVersionUID = 6004706395678775298L;

  private String id;

  private QuestionType type;

  private String body;

  private List<QuestionOption> options;

  private School school;

  Question() {

  }

  public Question(
      String id,
      QuestionType type,
      String body,
      List<QuestionOption> options,
      School school,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    super(createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime);
    this.id = id;
    this.type = type;
    this.body = body;
    this.options = options;
    this.school = school;
  }

  public String getId() {
    return id;
  }

  public QuestionType getType() { return type; }

  public String getBody() {
    return body;
  }

  public List<QuestionOption> getOptions() {
    return Collections.unmodifiableList(options);
  }

  public School getSchool() { return  school; }
}
