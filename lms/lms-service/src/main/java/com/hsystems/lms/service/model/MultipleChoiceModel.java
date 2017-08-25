package com.hsystems.lms.service.model;

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
public class MultipleChoiceModel extends QuestionModel
    implements Serializable {

  private static final long serialVersionUID = -7583454937348273301L;

  private List<QuestionOptionModel> options;

  public MultipleChoiceModel() {

  }

  public List<QuestionOptionModel> getOptions() {
    return CollectionUtils.isEmpty(options)
        ? Collections.emptyList()
        : Collections.unmodifiableList(options);
  }

  public void setOptions(List<QuestionOptionModel> options) {
    this.options = new ArrayList<>();
    this.options.addAll(options);
  }
}
