package com.hsystems.lms.repository.entity.quiz;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuizComponent implements GradableComponent, Serializable {

  private static final long serialVersionUID = -5123843132884732942L;

  @IndexField
  private String id;

  @IndexField
  private Quiz quiz;

  @IndexField
  private int order;

  QuizComponent() {

  }

  public QuizComponent(
      String id,
      Quiz quiz,
      int order) {

    this.id = id;
    this.quiz = quiz;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public long getScore() {
    return quiz.getScore();
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
        "QuizComponent{id=%s, quiz=%s, order=%s}",
        id, quiz, order);
  }
}
