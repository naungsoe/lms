package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Optional;

public final class HBaseSchoolRefMapper
    implements Mapper<Result, Optional<School>> {

  private static final byte[] SCHOOL_ID_QUALIFIER
      = Bytes.toBytes("school_id");
  private static final byte[] SCHOOL_NAME_QUALIFIER
      = Bytes.toBytes("school_name");

  public HBaseSchoolRefMapper() {

  }

  @Override
  public Optional<School> from(Result source) {
    if (HBaseUtils.containsColumn(source, SCHOOL_ID_QUALIFIER)) {
      String id = HBaseUtils.getString(source, SCHOOL_ID_QUALIFIER);
      String name = HBaseUtils.getString(source, SCHOOL_NAME_QUALIFIER);
      School school = new School.Builder(id, name).build();
      return Optional.of(school);
    }

    return Optional.empty();
  }
}