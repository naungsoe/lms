package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.QuestionType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection("questions")
public class Question extends Auditable implements Serializable {

  private static final long serialVersionUID = 6004706395678775298L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private QuestionType type;

  @IndexField(type = IndexFieldType.TEXT_GENERAL)
  private String body;

  @IndexField(type = IndexFieldType.LIST)
  private List<QuestionOption> options;

  @IndexField(type = IndexFieldType.OBJECT)
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

    this.id = id;
    this.type = type;
    this.body = body;
    this.options = options;
    this.school = school;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
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
