package com.hsystems.lms.provider.hbase;

import com.google.inject.Provider;

/**
 * Created by naungsoe on 19/9/16.
 */
public class HBaseClientProvider implements Provider<HBaseClient> {

  public HBaseClient get() {
    return new HBaseClient();
  }
}
