package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSchoolMapper
    implements Mapper<Result, Auditable<School>> {

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");
  private static final byte[] PERMISSIONS_QUALIFIER
      = Bytes.toBytes("permissions");

  private final HBasePreferencesMapper preferencesMapper;

  public HBaseSchoolMapper() {
    this.preferencesMapper = new HBasePreferencesMapper();
  }

  @Override
  public Auditable<School> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String name = HBaseUtils.getString(source, NAME_QUALIFIER);
    List<String> permissions = HBaseUtils.getStrings(
        source, PERMISSIONS_QUALIFIER);
    Preferences preferences = preferencesMapper.from(source);
    School school = new School.Builder(id, name)
        .permissions(permissions)
        .preferences(preferences)
        .build();

    HBaseAuditableMapper<School> auditableMapper
        = new HBaseAuditableMapper<>(school);
    return auditableMapper.from(source);
  }
}