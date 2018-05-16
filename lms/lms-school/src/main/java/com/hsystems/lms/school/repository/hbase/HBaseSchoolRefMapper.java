package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class HBaseSchoolRefMapper
    implements Mapper<List<Result>, Optional<School>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  private static final String SCHOOL_KEY_FORMAT = "%s_sch_";

  private final String parentKey;

  public HBaseSchoolRefMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public Optional<School> from(List<Result> source) {
    String schoolKey = String.format(SCHOOL_KEY_FORMAT, parentKey);
    Predicate<Result> schoolResult = HBaseUtils.isChildResult(schoolKey);
    Optional<Result> resultOptional = source.stream()
        .filter(schoolResult).findFirst();

    if (resultOptional.isPresent()) {
      Result result = resultOptional.get();
      String schoolRowKey = Bytes.toString(result.getRow());
      int startIndex = schoolRowKey.indexOf(schoolKey);
      String id = schoolRowKey.substring(startIndex);
      String name = HBaseUtils.getString(result, DATA_FAMILY, NAME_QUALIFIER);
      School school = new School.Builder(id, name).build();
      return Optional.of(school);
    }

    return Optional.empty();
  }
}