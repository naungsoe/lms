package com.hsystems.lms.common.query;

/**
 * Created by naungsoe on 10/8/16.
 */
public class Criterion {

  private Operator operator;

  private String field;

  private Object value;

  public Criterion(
      Operator operator, String field, Object value) {

    this.operator = operator;
    this.field = field;
    this.value = value;
  }

  public static Criterion createEqual(String field, Object value) {
    return new Criterion(Operator.EQUAL, field, value);
  }

  public static Criterion createNotEqual(String field, Object value) {
    return new Criterion(Operator.NOTEQUAL, field, value);
  }

  public static Criterion createGreaterThan(String field, Object value) {
    return new Criterion(Operator.GREATERTHAN, field, value);
  }

  public static Criterion createGreaterThanEqual(String field, Object value) {
    return new Criterion(Operator.GREATETHANEQUAL, field, value);
  }

  public static Criterion createLessThan(String field, Object value) {
    return new Criterion(Operator.LESSTHAN, field, value);
  }

  public static Criterion createLessThanEqual(String field, Object value) {
    return new Criterion(Operator.LESSTHANEQUAL, field, value);
  }

  public static Criterion createLike(String field, Object value) {
    return new Criterion(Operator.LIKE, field, value);
  }

  public static Criterion createEmpty() {
    return new Criterion(null, "", null);
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

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = operator.hashCode();
    result = result * prime + field.hashCode();
    return result * prime + value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    Criterion criterion = (Criterion) obj;
    return operator.equals(criterion.getOperator())
        && field.equals(criterion.getField())
        && value.equals(criterion.getValue());
  }

  @Override
  public String toString() {
    return String.format(
        "Criterion{operator=%s, field=%s, value=%s}",
        operator, field, value);
  }
}
