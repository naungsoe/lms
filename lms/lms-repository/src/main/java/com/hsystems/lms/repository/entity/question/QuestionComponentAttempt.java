package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponentAttempt;

/**
 * Created by naungsoe on 6/1/17.
 */
public abstract class QuestionComponentAttempt
    implements GradableComponentAttempt {

  @IndexField
  protected String id;

  @IndexField
  protected QuestionComponent component;

  @IndexField
  protected long score;

  @Override
  public String getId() {
    return id;
  }

  public QuestionComponent getComponent() {
    return component;
  }

  @Override
  public void gradeAttempt() {
    GradingStrategy strategy = component.getGradingStrategy();
    score = strategy.gradeAttempt(this);
  }

  @Override
  public long getScore() {
    return score;
  }
}