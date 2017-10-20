package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.GradableComponent;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuestionComponent<T extends Question>
    implements GradableComponent<QuestionComponentAttempt>, Serializable {

  private static final long serialVersionUID = 8801021552600205315L;

  @IndexField
  private String id;

  @IndexField
  private T question;

  @IndexField
  private long score;

  @IndexField
  private int order;

  QuestionComponent() {

  }

  public QuestionComponent(
      String id,
      T question,
      long score,
      int order) {

    this.id = id;
    this.question = question;
    this.score = score;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public T getQuestion() {
    return question;
  }

  public long getScore() {
    return score;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public void gradeAttempt(QuestionComponentAttempt attempt) {
    QuestionGradingStrategy gradingStrategy = question.getGradingStrategy();
    gradingStrategy.gradeAttempt(attempt.getAttempt(), score);
    attempt.setScore(gradingStrategy.getScore());
  }
}