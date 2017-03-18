package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuestionComponent implements Serializable, Component {

  private static final long serialVersionUID = -8886998378935720413L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.INTEGER)
  private int order;

  @IndexField(type = IndexFieldType.OBJECT)
  private Question question;

  QuestionComponent() {

  }

  public QuestionComponent(
      String id,
      int order,
      Question question) {

    this.id = id;
    this.order = order;
    this.question = question;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getOrder() {
    return order;
  }

  public Question getQuestion() {
    return question;
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

    QuestionComponent section = (QuestionComponent) obj;
    return id.equals(section.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "QuestionComponent{id=%s, order=%s, question=%s}",
        id, order, question);
  }
}
