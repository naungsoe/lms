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
public class MultipleResponseAttempt
    extends QuestionComponentAttempt implements Serializable {

  private static final long serialVersionUID = 5837561322960892074L;

  @IndexField
  private List<String> optionIds;

  MultipleResponseAttempt() {

  }

  public MultipleResponseAttempt(
      String id,
      QuestionComponent component,
      List<String> optionIds,
      long score) {

    this.id = id;
    this.component = component;
    this.optionIds = optionIds;
    this.score = score;
  }

  public Enumeration<String> getOptionIds() {
    return CollectionUtils.isEmpty(optionIds)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(optionIds);
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

    MultipleResponseAttempt attempt = (MultipleResponseAttempt) obj;
    return id.equals(attempt.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "MultipleResponseAttempt{id=%s, component=%s, optionIds=%s, score=%s}",
        id, component, StringUtils.join(optionIds, ","), score);
  }
}
