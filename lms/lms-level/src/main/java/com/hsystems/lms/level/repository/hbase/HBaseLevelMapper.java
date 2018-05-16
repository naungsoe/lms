package com.hsystems.lms.level.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseLevelMapper
    implements Mapper<List<Result>, Auditable<Level>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseLevelMapper() {

  }

  @Override
  public Auditable<Level> from(List<Result> source) {
    Optional<Result> mainResultOptional = source.stream()
        .filter(HBaseUtils.isMainResult()).findFirst();

    if (!mainResultOptional.isPresent()) {
      throw new IllegalArgumentException(
          "there is no main result found");
    }

    Result mainResult = mainResultOptional.get();
    String id = Bytes.toString(mainResult.getRow());
    String name = HBaseUtils.getString(
        mainResult, DATA_FAMILY, NAME_QUALIFIER);
    Level.Builder builder = new Level.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    Level level = builder.build();
    HBaseAuditableMapper<Level> auditableMapper
        = new HBaseAuditableMapper<>(level, id);
    return auditableMapper.from(source);
  }
}