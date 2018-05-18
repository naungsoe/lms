package com.hsystems.lms.subject.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSubjectMapper
    implements Mapper<Result, Auditable<Subject>> {

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseSubjectMapper() {

  }

  @Override
  public Auditable<Subject> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String name = HBaseUtils.getString(source, NAME_QUALIFIER);
    Subject.Builder builder = new Subject.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      builder.school(schoolOptional.get());
    }

    Subject subject = builder.build();
    HBaseAuditableMapper<Subject> auditableMapper
        = new HBaseAuditableMapper<>(subject);
    return auditableMapper.from(source);
  }
}