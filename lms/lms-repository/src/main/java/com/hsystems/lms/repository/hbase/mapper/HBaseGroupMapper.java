package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Group;
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
public class HBaseGroupMapper extends HBaseMapper<Group> {

  @Override
  public List<Group> getEntities (
      List<Result> results, List<Mutation> mutations) {

    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<Group> groups = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        Group group = getEntity(result, results, timestamp);
        groups.add(group);
      }
    });
    return groups;
  }

  public Group getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String name = getName(mainResult, timestamp);
    List<Permission> permissions = getPermissions(mainResult, ",", timestamp);

    List<User> members = new ArrayList<>();
    results.stream().filter(isMemberResult(id))
        .forEach(memberResult -> {
          User member = getMember(memberResult, timestamp);
          members.add(member);
        });

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);

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

    return new Group(
        id,
        name,
        permissions,
        members,
        school,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public Group getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(Group entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Group entity, long timestamp) {
    return new ArrayList<>();
  }
}
