package com.hsystems.lms.subject.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSubjectMapper
    implements Mapper<List<Result>, Auditable<Subject>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  public HBaseSubjectMapper() {

  }

  @Override
  public Auditable<Subject> from(List<Result> source) {
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
    Subject.Builder builder = new Subject.Builder(id, name);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    Subject subject = builder.build();
    HBaseAuditableMapper<Subject> auditableMapper
        = new HBaseAuditableMapper<>(subject, id);
    return auditableMapper.from(source);
  }
}