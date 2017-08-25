package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class MultipleResponseComponent
    extends QuestionComponent implements GradableComponent, Serializable {

  private static final long serialVersionUID = 7470417312905531691L;

  @IndexField
  private MultipleResponse question;

  MultipleResponseComponent() {

  }

  public MultipleResponseComponent(
      String id,
      MultipleResponse question,
      int order,
      long score) {

    this.id = id;
    this.question = question;
    this.order = order;
    this.score = score;
  }

  @Override
  public Question getQuestion() {
    return question;
  }

  @Override
  public GradingStrategy getGradingStrategy() {
    return new MultipleResponseStrategy(this);
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

    MultipleResponseComponent component = (MultipleResponseComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "MultipleResponseComponent{id=%s, question=%s, order=%s, score=%s}",
        id, question, order, score);
  }
}
