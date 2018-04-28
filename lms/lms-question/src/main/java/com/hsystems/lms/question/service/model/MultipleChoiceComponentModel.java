package com.hsystems.lms.service.model.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultipleChoiceComponentModel
    extends QuestionComponentModel<MultipleChoiceModel> implements Serializable {

  public MultipleChoiceComponentModel() {

  }
}
