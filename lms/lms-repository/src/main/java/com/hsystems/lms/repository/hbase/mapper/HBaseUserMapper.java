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
public class HBaseUserMapper extends HBaseMapper<User> {

  @Override
  public User getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    String account = getAccount(mainResult);
    String password = getPassword(mainResult);
    String salt = getSalt(mainResult);
    String firstName = getFirstName(mainResult);
    String lastName = getLastName(mainResult);
    LocalDateTime dateOfBirth = getDateOfBirth(mainResult);
    String gender = getGender(mainResult);
    String mobile = getMobile(mainResult);
    String email = getEmail(mainResult);
    String locale = getLocale(mainResult);
    String dateFormat = getDateFormat(mainResult);
    String dateTimeFormat = getDateTimmeFormat(mainResult);
    List<Permission> permissions = getPermissions(mainResult, ",");


    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);

    List<Group> groups = new ArrayList<>();
    results.stream().filter(isGroupResult(id))
        .forEach(groupResult -> {
          Group group = getGroup(groupResult);
          groups.add(group);
        });

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
  public List<Put> getPuts(User entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(User entity, long timestamp) {
    return new ArrayList<>();
  }
}
