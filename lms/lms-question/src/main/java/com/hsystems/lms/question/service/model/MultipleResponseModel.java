package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MultipleResponseModel
    extends QuestionModel implements Serializable {

  private static final long serialVersionUID = -3665697059272165503L;

  private List<ChoiceOptionModel> options;

  public MultipleResponseModel() {

  }

  public List<ChoiceOptionModel> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyList() : options;
  }

  public void setOptions(List<ChoiceOptionModel> options) {
    this.options = new ArrayList<>();
    this.options.addAll(options);
  }
}