package com.hsystems.lms.repository.hbase.patch;

import java.io.Serializable;

public class HBaseOperation implements Serializable {

  private static final long serialVersionUID = -8213602080257157927L;

  private String rowKey;

  private byte[] family;

  private byte[] qualifier;

  private byte[] value;

  HBaseOperation() {

  }

  public HBaseOperation(
      String rowKey,
      byte[] family,
      byte[] qualifier,
      byte[] value) {

    this.rowKey = rowKey;
    this.family = family;
    this.qualifier = qualifier;
    this.value = value;
  }

  public String getRowKey() {
    return rowKey;
  }

  public byte[] getFamily() {
    return family;
  }

  public byte[] getQualifier() {
    return qualifier;
  }

  public byte[] getValue() {
    return value;
  }
}
