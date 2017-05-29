package com.hsystems.lms.service.model;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public abstract class ComponentModel implements Serializable {

  private String id;

  private String type;

  private int order;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
