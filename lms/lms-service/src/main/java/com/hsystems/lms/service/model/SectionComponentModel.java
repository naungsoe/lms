package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public class SectionComponentModel
    extends CompositeComponentModel implements Serializable {

  private static final long serialVersionUID = -3508415938486476541L;

  private String instructions;

  public SectionComponentModel() {

  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }
}
