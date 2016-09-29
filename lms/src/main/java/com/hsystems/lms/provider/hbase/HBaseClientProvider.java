package com.hsystems.lms.provider.hbase;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Properties;

/**
 * Created by administrator on 23/9/16.
 */
public class HBaseClientProvider implements Provider<HBaseClient> {

  private Provider<Properties> propertiesProvider;

  @Inject
  HBaseClientProvider(Provider<Properties> propertiesProvider) {
    this.propertiesProvider = propertiesProvider;
  }

  public HBaseClient get() {
    return new HBaseClient(propertiesProvider);
  }
}
