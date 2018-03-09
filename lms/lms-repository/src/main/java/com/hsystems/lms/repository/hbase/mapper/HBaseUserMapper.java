package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Mutation;
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
public class HBaseUserMapper extends HBaseAbstractMapper<User> {

  @Override
  public List<User> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<User> users = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<User> userOptional
            = getEntity(result, results, timestamp);

        if (userOptional.isPresent()) {
          users.add(userOptional.get());
        }
      }
    });
    return users;
  }

  private Optional<User> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String firstName = getFirstName(mainResult, timestamp);
    String lastName = getLastName(mainResult, timestamp);
    String account = getAccount(mainResult, timestamp);
    String password = getPassword(mainResult, timestamp);
    String salt = getSalt(mainResult, timestamp);
    LocalDateTime dateOfBirth = getDateOfBirth(mainResult, timestamp);
    String gender = getGender(mainResult, timestamp);
    String mobile = getMobile(mainResult, timestamp);
    String email = getEmail(mainResult, timestamp);
    String locale = getLocale(mainResult, timestamp);
    String timeFormat = getTimeFormat(mainResult, timestamp);
    String dateFormat = getDateFormat(mainResult, timestamp);
    String dateTimeFormat = getDateTimeFormat(mainResult, timestamp);
    List<String> permissions = getPermissions(mainResult, timestamp);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);
    List<Group> groups = getGroups(results, id, timestamp);

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
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(User entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(User entity, long timestamp) {
    return Collections.emptyList();
  }
}
