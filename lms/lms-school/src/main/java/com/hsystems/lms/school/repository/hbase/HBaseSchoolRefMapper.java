package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public final class HBaseSchoolRefMapper
    implements Mapper<Result, School> {

  private static final byte[] ID_QUALIFIER = Bytes.toBytes("school_id");
  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("school_name");

  public HBaseSchoolRefMapper() {

  }

  @Override
  public School from(Result source) {
    String id = HBaseUtils.getString(source, ID_QUALIFIER);
    String name = HBaseUtils.getString(source, NAME_QUALIFIER);
    return new School.Builder(id, name).build();
  }
}