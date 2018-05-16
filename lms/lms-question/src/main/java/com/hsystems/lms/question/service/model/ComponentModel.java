package com.hsystems.lms.question.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ComponentModel implements Serializable {

  private static final long serialVersionUID = -3390633991000147158L;

  private int order;

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}