package com.hsystems.lms.domain.model.hbase;

import com.hsystems.lms.domain.model.Empty;

import org.apache.hadoop.hbase.client.Result;

/**
 * Created by administrator on 8/8/16.
 */
public final class ResultEmpty extends Result implements Empty {

  public ResultEmpty() {

  }

  public boolean isEmpty() {
    return true;
  }
}
