package com.hsystems.lms.authentication.repository.hbase;

import com.hsystems.lms.authentication.repository.entity.SignInLog;
import com.hsystems.lms.common.mapper.Mapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSignInLogMapper
    implements Mapper<Result, SignInLog> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseSignInLogMapper() {

  }

  @Override
  public SignInLog from(Result source) {
    return null;
  }
}