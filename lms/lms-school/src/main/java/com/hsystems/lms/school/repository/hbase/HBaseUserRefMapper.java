package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseUserRefMapper
    implements Mapper<Result, User> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] FIRST_NAME_QUALIFIER = Bytes.toBytes("fname");
  private static final byte[] LAST_NAME_QUALIFIER = Bytes.toBytes("lname");

  private final String parentKey;

  public HBaseUserRefMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public User from(Result source) {
    String userKey = Bytes.toString(source.getRow());
    int startIndex = userKey.indexOf(parentKey);
    String id = userKey.substring(startIndex);
    String firstName = HBaseUtils.getString(
        source, DATA_FAMILY, FIRST_NAME_QUALIFIER);
    String lastName = HBaseUtils.getString(
        source, DATA_FAMILY, LAST_NAME_QUALIFIER);

    return new User() {
      @Override
      public String getId() {
        return id;
      }

      @Override
      public String getFirstName() {
        return firstName;
      }

      @Override
      public String getLastName() {
        return lastName;
      }
    };
  }
}