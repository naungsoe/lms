package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Mutation;
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
public class HBaseSchoolMapper extends HBaseAbstractMapper<School> {

  @Override
  public List<School> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<School> schools = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<School> schoolOptional
            = getEntity(result, results, timestamp);

        if (schoolOptional.isPresent()) {
          schools.add(schoolOptional.get());
        }
      }
    });
    return schools;
  }

  private Optional<School> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String name = getName(mainResult, timestamp);
    String locale = getLocale(mainResult, timestamp);
    String dateFormat = getDateFormat(mainResult, timestamp);
    String dateTimeFormat = getDateTimeFormat(mainResult, timestamp);
    List<Permission> permissions = getPermissions(mainResult, timestamp);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult, timestamp);
    LocalDateTime createdDateTime = getDateTime(createdByResult, timestamp);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get(), timestamp) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get(), timestamp) : null;

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
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(School entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(School entity, long timestamp) {
    return Collections.emptyList();
  }
}