package com.hsystems.lms.group.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.hbase.HBaseUtils;
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
public final class HBaseGroupMapper
    implements Mapper<List<Result>, Auditable<Group>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseGroupMapper() {

  }

  @Override
  public Auditable<Group> from(List<Result> source) {
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
    Group.Builder builder = new Group.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    Group group = builder.build();
    HBaseAuditableMapper<Group> auditableMapper
        = new HBaseAuditableMapper<>(group, id);
    return auditableMapper.from(source);
  }
}