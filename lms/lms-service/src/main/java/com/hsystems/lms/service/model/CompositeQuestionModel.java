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
public class CompositeQuestionModel extends QuestionModel
    implements Serializable {

  private static final long serialVersionUID = 5596258332789130280L;

  private List<QuestionComponentModel> components;

  public CompositeQuestionModel() {

  }

  public List<QuestionComponentModel> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList()
        : Collections.unmodifiableList(components);
  }

  public void setComponents(List<QuestionComponentModel> components) {
    this.components = new ArrayList<>();
    this.components.addAll(components);
  }
}
