package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class MultipleChoiceComponent
    extends QuestionComponent implements GradableComponent, Serializable {

  private static final long serialVersionUID = 1762918039828818441L;

  @IndexField
  private MultipleChoice question;

  MultipleChoiceComponent() {

  }

  public MultipleChoiceComponent(
      String id,
      MultipleChoice question,
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
    return new MultipleChoiceStrategy(this);
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

    MultipleChoiceComponent component = (MultipleChoiceComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "MultipleChoiceComponent{id=%s, question=%s, order=%s, score=%s}",
        id, question, order, score);
  }
}
