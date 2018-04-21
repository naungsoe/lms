package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

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
public class HBaseSchoolMapper extends HBaseEntityMapper<School> {

  @Override
  public List<School> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<School> schools = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<School> schoolOptional = getEntity(result, results);

      if (schoolOptional.isPresent()) {
        schools.add(schoolOptional.get());
      }
    });

    return schools;
  }

  private Optional<School> getEntity(Result mainResult, List<Result> results) {
    String id = Bytes.toString(mainResult.getRow());
    String name = getName(mainResult);
    String locale = getLocale(mainResult);
    String dateFormat = getDateFormat(mainResult);
    String dateTimeFormat = getDateTimeFormat(mainResult);
    List<String> permissions = getPermissions(mainResult);

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

    School school = new School.Builder(id, name)
        .locale(locale)
        .dateFormat(dateFormat)
        .dateTimeFormat(dateTimeFormat)
        .permissions(permissions)
        .createdBy(createdBy)
        .createdDateTime(createdDateTime)
        .modifiedBy(modifiedBy)
        .modifiedDateTime(modifiedDateTime)
        .build();
    return Optional.of(school);
  }

  @Override
  public Optional<School> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}