package com.hsystems.lms.service.model.question;

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
public final class CompositeQuestionModel extends QuestionModel
    implements Serializable {

  private static final long serialVersionUID = 1866222829330588636L;

  private List<QuestionComponentModel> components;

  public CompositeQuestionModel() {

  }

  public List<QuestionComponentModel> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList() : components;
  }

  public void setComponents(List<QuestionComponentModel> components) {
    this.components = new ArrayList<>();
    this.components.addAll(components);
  }
}
