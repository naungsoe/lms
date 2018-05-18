package com.hsystems.lms.component.repository.hbase;

import org.apache.hadoop.hbase.client.Result;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface HBaseComponentMapperFactory {

  HBaseComponentMapper create(Result result);
}