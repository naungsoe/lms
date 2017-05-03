package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseSchoolMapper extends HBaseMapper<School> {

  @Override
  public List<School> getEntities(List<Result> results) {
    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<School> schools = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      School school = getEntity(result, results);
      schools.add(school);
    });
    return schools;
  }

  private School getEntity(Result mainResult, List<Result> results) {
    String id = Bytes.toString(mainResult.getRow());
    String name = getName(mainResult);
    String locale = getLocale(mainResult);
    String dateFormat = getDateFormat(mainResult);
    String dateTimeFormat = getDateTimmeFormat(mainResult);
    List<Permission> permissions = getPermissions(mainResult, ",");

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get()) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get()) : null;

    return new School(
        id,
        name,
        locale,
        dateFormat,
        dateTimeFormat,
        permissions,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public School getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }

  @Override
  public List<Put> getPuts(School entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(School entity, long timestamp) {
    return new ArrayList<>();
  }
}