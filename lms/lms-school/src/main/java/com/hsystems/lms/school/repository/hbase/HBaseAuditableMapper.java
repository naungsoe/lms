package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class HBaseAuditableMapper<T extends Entity>
    implements Mapper<List<Result>, Auditable<T>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] DATE_TIME_QUALIFIER = Bytes.toBytes("datetime");

  private static final String CREATED_BY_KEY_FORMAT = "%s_cre_";
  private static final String MODIFIED_BY_KEY_FORMAT = "" + "%s_mod_";

  private final T entity;

  private final String rowKey;

  public HBaseAuditableMapper(T entity, String rowKey) {
    this.entity = entity;
    this.rowKey = rowKey;
  }

  @Override
  public Auditable<T> from(List<Result> source) {
    Auditable.Builder<T> builder = new Auditable.Builder<>(entity);
    String createdByKey = String.format(CREATED_BY_KEY_FORMAT, rowKey);
    Predicate<Result> createdByResult = HBaseUtils.isChildResult(createdByKey);
    Optional<Result> resultOptional = source.stream()
        .filter(createdByResult).findFirst();

    if (resultOptional.isPresent()) {
      Result result = resultOptional.get();
      HBaseUserRefMapper userMapper = new HBaseUserRefMapper(createdByKey);
      User createdBy = userMapper.from(result);
      LocalDateTime createdOn = HBaseUtils.getDateTime(
          result, DATA_FAMILY, DATE_TIME_QUALIFIER);
      builder.createdBy(createdBy).createdOn(createdOn);
    }

    String modifiedByKey = String.format(MODIFIED_BY_KEY_FORMAT, rowKey);
    Predicate<Result> modifiedByResult
        = HBaseUtils.isChildResult(modifiedByKey);
    resultOptional = source.stream().filter(modifiedByResult).findFirst();

    if (resultOptional.isPresent()) {
      Result result = resultOptional.get();
      HBaseUserRefMapper userMapper = new HBaseUserRefMapper(modifiedByKey);
      User modifiedBy = userMapper.from(result);
      LocalDateTime modifiedOn = HBaseUtils.getDateTime(
          result, DATA_FAMILY, DATE_TIME_QUALIFIER);
      builder.modifiedBy(modifiedBy).modifiedOn(modifiedOn);
    }

    return builder.build();
  }
}