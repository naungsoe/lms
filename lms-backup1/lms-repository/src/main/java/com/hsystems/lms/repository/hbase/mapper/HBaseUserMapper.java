package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Group;
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
public class HBaseUserMapper extends HBaseEntityMapper<User> {

  @Override
  public List<User> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<User> users = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<User> userOptional = getEntity(result, results);

      if (userOptional.isPresent()) {
        users.add(userOptional.get());
      }
    });

    return users;
  }

  private Optional<User> getEntity(Result mainResult, List<Result> results) {
    String id = Bytes.toString(mainResult.getRow());
    String firstName = getFirstName(mainResult);
    String lastName = getLastName(mainResult);
    String account = getAccount(mainResult);
    String password = getPassword(mainResult);
    String salt = getSalt(mainResult);
    LocalDateTime dateOfBirth = getDateOfBirth(mainResult);
    String gender = getGender(mainResult);
    String mobile = getMobile(mainResult);
    String email = getEmail(mainResult);
    String locale = getLocale(mainResult);
    String timeFormat = getTimeFormat(mainResult);
    String dateFormat = getDateFormat(mainResult);
    String dateTimeFormat = getDateTimeFormat(mainResult);
    List<String> permissions = getPermissions(mainResult);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);
    List<Group> groups = getGroups(results, id);

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

    User user = new User.Builder(id, firstName, lastName)
        .account(account)
        .password(password)
        .salt(salt)
        .dateOfBirth(dateOfBirth)
        .gender(gender)
        .mobile(mobile)
        .email(email)
        .locale(locale)
        .timeFormat(timeFormat)
        .dateFormat(dateFormat)
        .dateTimeFormat(dateTimeFormat)
        .permissions(permissions)
        .school(school)
        .groups(groups)
        .createdBy(createdBy)
        .createdDateTime(createdDateTime)
        .modifiedBy(modifiedBy)
        .modifiedDateTime(modifiedDateTime)
        .build();
    return Optional.of(user);
  }

  @Override
  public Optional<User> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}
