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
public class HBaseUserMapper extends HBaseMapper<User> {

  @Override
  public List<User> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<User> users = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        User user = getEntity(result, results, timestamp);
        users.add(user);
      }
    });
    return users;
  }

  private User getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String account = getAccount(mainResult, timestamp);
    String password = getPassword(mainResult, timestamp);
    String salt = getSalt(mainResult, timestamp);
    String firstName = getFirstName(mainResult, timestamp);
    String lastName = getLastName(mainResult, timestamp);
    LocalDateTime dateOfBirth = getDateOfBirth(mainResult, timestamp);
    String gender = getGender(mainResult, timestamp);
    String mobile = getMobile(mainResult, timestamp);
    String email = getEmail(mainResult, timestamp);
    String locale = getLocale(mainResult, timestamp);
    String dateFormat = getDateFormat(mainResult, timestamp);
    String dateTimeFormat = getDateTimeFormat(mainResult, timestamp);
    List<Permission> permissions = getPermissions(mainResult, ",", timestamp);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);

    List<Group> groups = new ArrayList<>();
    results.stream().filter(isGroupResult(id))
        .forEach(groupResult -> {
          Group group = getGroup(groupResult, timestamp);
          groups.add(group);
        });

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

    return new User(
        id,
        account,
        password,
        salt,
        firstName,
        lastName,
        dateOfBirth,
        gender,
        mobile,
        email,
        locale,
        dateFormat,
        dateTimeFormat,
        permissions,
        school,
        groups,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public User getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(User entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(User entity, long timestamp) {
    return new ArrayList<>();
  }
}
