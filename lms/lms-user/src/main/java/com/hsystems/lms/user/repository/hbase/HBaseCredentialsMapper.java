package com.hsystems.lms.user.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.user.repository.entity.Credentials;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseCredentialsMapper
    implements Mapper<Result, Credentials> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] ACCOUNT_QUALIFIER = Bytes.toBytes("account");
  private static final byte[] PASSWORD_QUALIFIER = Bytes.toBytes("password");
  private static final byte[] SALT_QUALIFIER = Bytes.toBytes("salt");

  public HBaseCredentialsMapper() {

  }

  @Override
  public Credentials from(Result source) {
    String account = HBaseUtils.getString(
        source, DATA_FAMILY, ACCOUNT_QUALIFIER);
    String password = HBaseUtils.getString(
        source, DATA_FAMILY, PASSWORD_QUALIFIER);
    String salt = HBaseUtils.getString(
        source, DATA_FAMILY, SALT_QUALIFIER);
    return new Credentials(account, password, salt);
  }
}