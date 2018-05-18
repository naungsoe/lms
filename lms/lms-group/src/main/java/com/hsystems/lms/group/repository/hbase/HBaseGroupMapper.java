package com.hsystems.lms.group.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseGroupMapper
    implements Mapper<Result, Auditable<Group>> {

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseGroupMapper() {

  }

  @Override
  public Auditable<Group> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String name = HBaseUtils.getString(source, NAME_QUALIFIER);
    Group.Builder builder = new Group.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      builder.school(schoolOptional.get());
    }

    Group group = builder.build();
    HBaseAuditableMapper<Group> auditableMapper
        = new HBaseAuditableMapper<>(group);
    return auditableMapper.from(source);
  }
}