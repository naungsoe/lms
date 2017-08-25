package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

/**
 * Created by naungsoe on 19/12/16.
 */
public abstract class QuestionComponent implements GradableComponent {

  @IndexField
  protected String id;

  @IndexField
  protected int order;

  @IndexField
  protected long score;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public long getScore() {
    return score;
  }

  public abstract Question getQuestion();

  public abstract GradingStrategy getGradingStrategy();
}
