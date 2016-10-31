package com.hbase.jersey;

import com.google.inject.AbstractModule;

import com.hbase.jersey.repository.UserRepository;
import com.hbase.jersey.repository.hbase.HBaseUserRepository;

/**
 * Created by administrator on 27/10/16.
 */
public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UserRepository.class).to(HBaseUserRepository.class);
    bind(MyResource.class);
  }
}
