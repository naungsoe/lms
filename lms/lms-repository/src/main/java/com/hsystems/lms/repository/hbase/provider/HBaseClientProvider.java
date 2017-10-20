package com.hsystems.lms.repository.hbase.provider;

import com.google.inject.Provider;

/**
 * Created by naungsoe on 19/9/16.
 */
public class HBaseClientProvider implements Provider<HBaseClient> {

  private volatile HBaseClient client;

  HBaseClientProvider() {

  }

  public HBaseClient get() {
    HBaseClient instance = client;

    if (instance == null) {
      synchronized (this) {
        instance = client;

        if (instance == null) {
          client = new HBaseClient();
          instance = client;
        }
      }
    }

    return instance;
  }
}
