package com.hsystems.lms.domain.model.hbase;

import com.hsystems.lms.domain.model.Null;

import org.apache.hadoop.hbase.client.Result;

/**
 * Created by administrator on 8/8/16.
 */
public final class NullResult extends Result implements Null {

  public NullResult() {

  }

  public boolean isNull() {
    return true;
  }
}
