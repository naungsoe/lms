package com.hsystems.lms.common.query;

import java.io.Serializable;

/**
 * Created by naungsoe on 24/11/16.
 */
public final class SortKey implements Serializable {

  private static final long serialVersionUID = 350022961949090712L;

  private String field;

  private SortOrder order;

  SortKey() {

  }

  public SortKey(String field, SortOrder order) {
    this.field = field;
    this.order = order;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public SortOrder getOrder() {
    return order;
  }

  public void setOrder(SortOrder order) {
    this.order = order;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = field.hashCode();
    return result * prime + order.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    SortKey sortKey = (SortKey) obj;
    return field.equals(sortKey.getField())
        && order.equals(sortKey.getOrder());
  }

  @Override
  public String toString() {
    return String.format(
        "SortKey{field=%s, order=%s}",
        field, order);
  }
}
