package com.hsystems.lms.common.patch;

public final class Operation {

  private String op;

  private String path;

  private String value;

  Operation() {

  }

  public Operation(
      String op,
      String path,
      String value) {

    this.op = op;
    this.path = path;
    this.value = value;
  }

  public String getOp() {
    return op;
  }

  public String getPath() {
    return path;
  }

  public String getValue() {
    return value;
  }
}
