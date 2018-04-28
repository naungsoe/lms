package com.hsystems.lms.service.model.course;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.CompositeComponentModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TopicComponentModel extends CompositeComponentModel
    implements Serializable {

  private static final long serialVersionUID = -1064549891610902549L;

  private String title;

  private String instructions;

  public TopicComponentModel() {

  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }
}
