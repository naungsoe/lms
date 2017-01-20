package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseGroupMapper extends HBaseMapper<Group> {

  @Override
  public Group getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    String name = getName(mainResult);
    List<Permission> permissions = getPermissions(mainResult, ",");

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);

    List<User> members = new ArrayList<>();
    results.stream().filter(isMemberResult(id))
        .forEach(x -> members.add(getMember(x)));

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> modifiedByResultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = modifiedByResultOptional.isPresent()
        ? getModifiedBy(modifiedByResultOptional.get()) : null;
    LocalDateTime modifiedDateTime = modifiedByResultOptional.isPresent()
        ? getDateTime(modifiedByResultOptional.get()) : LocalDateTime.MIN;

    return new Group(
        id,
        name,
        permissions,
        school,
        members,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public List<Put> getPuts(Group entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Group entity, long timestamp) {
    return new ArrayList<>();
  }
}
