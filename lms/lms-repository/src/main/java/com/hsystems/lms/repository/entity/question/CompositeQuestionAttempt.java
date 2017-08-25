package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public class CompositeQuestionAttempt
    extends QuestionComponentAttempt implements Serializable {

  private static final long serialVersionUID = 9214972455831002172L;

  @IndexField
  private List<QuestionComponentAttempt> attempts;

  CompositeQuestionAttempt() {

  }

  public CompositeQuestionAttempt(
      String id,
      List<QuestionComponentAttempt> attempts,
      long score) {

    this.id = id;
    this.attempts = attempts;
    this.score = score;
  }

  public Enumeration<QuestionComponentAttempt> getAttempts() {
    return CollectionUtils.isEmpty(attempts)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(attempts);
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

    CompositeQuestionAttempt attempt = (CompositeQuestionAttempt) obj;
    return id.equals(attempt.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "CompositeQuestionAttempt{id=%s, attempts=%s, score=%s}",
        id, StringUtils.join(attempts, ","), score);
  }
}
