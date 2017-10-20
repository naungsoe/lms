package com.hsystems.lms.service.model.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.ResourceModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResourceModel<T extends QuestionModel>
    extends ResourceModel implements Serializable {

  private static final long serialVersionUID = 5590127587196438486L;

  private T question;

  public QuestionResourceModel() {

  }

  public T getQuestion() {
    return question;
  }

  public void setQuestion(T question) {
    this.question = question;
  }
}
