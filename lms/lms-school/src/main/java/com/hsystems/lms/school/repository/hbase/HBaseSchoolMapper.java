package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSchoolMapper
    implements Mapper<List<Result>, Auditable<School>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");
  private static final byte[] PERMISSIONS_QUALIFIER
      = Bytes.toBytes("permissions");

  private final HBasePreferencesMapper preferencesMapper;

  public HBaseSchoolMapper() {
    this.preferencesMapper = new HBasePreferencesMapper();
  }

  @Override
  public Auditable<School> from(List<Result> source) {
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
    List<String> permissions = HBaseUtils.getStrings(
        mainResult, DATA_FAMILY, PERMISSIONS_QUALIFIER);
    Preferences preferences = preferencesMapper.from(mainResult);
    School school = new School.Builder(id, name)
        .permissions(permissions)
        .preferences(preferences)
        .build();

    HBaseAuditableMapper<School> auditableMapper
        = new HBaseAuditableMapper<>(school, id);
    return auditableMapper.from(source);
  }
}