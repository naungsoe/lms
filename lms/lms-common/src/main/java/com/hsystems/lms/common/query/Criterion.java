package com.hsystems.lms.common.query;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 10/8/16.
 */
public class Criterion {

  private Operator operator;

  private String field;

  private List<Object> values;

  Criterion() {

  }

  public Criterion(
      Operator operator, String field, Object[] values) {

    this.operator = operator;
    this.field = field;
    this.values = Arrays.asList(values);
  }

  public static Criterion createEqual(String field, Object... values) {
    return new Criterion(Operator.EQUAL, field, values);
  }

  public static Criterion createNotEqual(String field, Object... values) {
    return new Criterion(Operator.NOT_EQUAL, field, values);
  }

  public static Criterion createGreaterThanEqual(
      String field, Object... values) {

    return new Criterion(Operator.GREATER_THAN_EQUAL, field, values);
  }

  public static Criterion createLessThanEqual(String field, Object... values) {
    return new Criterion(Operator.LESS_THAN_EQUAL, field, values);
  }

  public static Criterion createLike(String field, Object... values) {
    return new Criterion(Operator.LIKE, field, values);
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public List<Object> getValues() {
    return CollectionUtils.isEmpty(values)
        ? Collections.emptyList() : Collections.unmodifiableList(values);
  }

  public void setValues(List<Object> values) {
    this.values = new ArrayList();
    this.values.addAll(values);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = operator.hashCode();
    result = result * prime + field.hashCode();
    return result * prime + values.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Criterion criterion = (Criterion) obj;
    return operator.equals(criterion.getOperator())
        && field.equals(criterion.getField())
        && values.equals(criterion.getValues());
  }

  @Override
  public String toString() {
    return String.format(
        "Criterion{operator=%s, field=%s, values=%s}",
        operator, field, StringUtils.join(values, " "));
  }
}
