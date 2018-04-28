package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.hadoop.hbase.client.Result;

import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSchoolMapper
    implements Mapper<Auditable<School>, List<Result>> {

  @Override
  public Auditable<School> from(List<Result> source) {
    return null;
  }
}