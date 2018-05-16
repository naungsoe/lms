package com.hsystems.lms.question.service.model.special;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.question.service.model.QuestionModel;
import com.hsystems.lms.question.service.model.QuestionType;

import java.io.Serializable;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnknownQuestionModel
    extends QuestionModel implements Serializable {

  private static final long serialVersionUID = -1592997810312166513L;

  public UnknownQuestionModel() {

  }

  @Override
  public QuestionType getType() {
    return QuestionType.UNKNOWN;
  }

  @Override
  public String getBody() {
    return "";
  }

  @Override
  public String getHint() {
    return "";
  }

  @Override
  public String getExplanation() {
    return "";
  }
}