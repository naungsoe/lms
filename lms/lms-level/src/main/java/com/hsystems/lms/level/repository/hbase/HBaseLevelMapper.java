package com.hsystems.lms.level.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseLevelMapper
    implements Mapper<Result, Auditable<Level>> {

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseLevelMapper() {

  }

  @Override
  public Auditable<Level> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String name = HBaseUtils.getString(source, NAME_QUALIFIER);
    Level.Builder builder = new Level.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      builder.school(schoolOptional.get());
    }

    Level level = builder.build();
    HBaseAuditableMapper<Level> auditableMapper
        = new HBaseAuditableMapper<>(level);
    return auditableMapper.from(source);
  }
}