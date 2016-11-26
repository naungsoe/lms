package com.hsystems.lms.common.query;

import javax.swing.*;

/**
 * Created by administrator on 24/11/16.
 */
public class SortKey {

  private String field;

  private SortOrder order;

  public SortKey(String field, SortOrder order) {
    this.field = field;
    this.order = order;
  }

  public String getField() {
    return field;
  }

  public SortOrder getOrder() {
    return order;
  }
}
