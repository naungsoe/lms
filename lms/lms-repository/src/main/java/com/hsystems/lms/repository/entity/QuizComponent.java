package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuizComponent implements Serializable, Component {

  private static final long serialVersionUID = 863456205775216283L;

  @IndexField
  private String id;

  @IndexField
  private int order;

  @IndexField
  private Quiz quiz;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      int order,
      Quiz quiz) {

    this.id = id;
    this.order = order;
    this.quiz = quiz;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getOrder() {
    return order;
  }

  public Quiz getQuiz() {
    return quiz;
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

    QuizComponent component = (QuizComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "QuizComponent{id=%s, order=%s, quiz=%s}",
        id, order, quiz);
  }
}
