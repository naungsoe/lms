package com.hsystems.lms.service.search;

/**
 * Created by administrator on 10/8/16.
 */
public final class Criterion {

  private final Operator operator;

  private final String field;

  private final String value;

  public Criterion(
      Operator operator, String field, String value) {
    this.operator = operator;
    this.field = field;
    this.value = value;
  }

  public Criterion createNull(String field, String value) {
    return new Criterion(Operator.NULL, field, value);
  }

  public Criterion createNotNull(String field, String value) {
    return new Criterion(Operator.NOTNULL, field, value);
  }

  public Criterion createEqual(String field, String value) {
    return new Criterion(Operator.EQUAL, field, value);
  }

  public Criterion createNotEqual(String field, String value) {
    return new Criterion(Operator.NOTEQUAL, field, value);
  }

  public Criterion createGreaterThan(String field, String value) {
    return new Criterion(Operator.GREATERTHAN, field, value);
  }

  public Criterion createGreaterThanEqual(String field, String value) {
    return new Criterion(Operator.GREATETHANEQUAL, field, value);
  }

  public Criterion createLessThan(String field, String value) {
    return new Criterion(Operator.LESSTHAN, field, value);
  }

  public Criterion createLessThanEqual(String field, String value) {
    return new Criterion(Operator.LESSTHANEQUAL, field, value);
  }
}
