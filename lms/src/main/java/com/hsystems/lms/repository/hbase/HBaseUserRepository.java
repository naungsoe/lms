package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Group;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.User;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  private HBaseClient client;

  @Inject
  HBaseUserRepository(HBaseClient client) {
    this.client = client;
  }

  public Optional<User> findBy(String id)
      throws RepositoryException {

    try {
      Scan scan = getRowFilterScan(id);
      List<Result> results = client.scan(scan, Constants.TABLE_USERS);

      if (CollectionUtils.isEmpty(results)) {
        return Optional.empty();
      }

      return getUser(results);

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving user", e);
    }
  }

  protected Optional<User> getUser(List<Result> results)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    User user = (User) ReflectionUtils.getInstance(User.class);
    List<Group> groups = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());

      if (rowKey.contains(Constants.SEPARATOR_SCHOOL)) {
        ReflectionUtils.setValue(
            user, Constants.FIELD_SCHOOL, getSchool(rowKey, result));

      } else if (rowKey.contains(Constants.SEPARATOR_GROUP)) {
        groups.add(getGroup(rowKey, result));

      } else {
        ReflectionUtils.setValue(user, Constants.FIELD_ID, rowKey);
        populateUserFields(user, result);
      }
    }

    ReflectionUtils.setValue(user, Constants.FIELD_GROUPS, groups);
    return Optional.of(user);
  }

  protected void populateUserFields(User user, Result result)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    ReflectionUtils.setValue(user, Constants.FIELD_PASSWORD,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PASSWORD));
    ReflectionUtils.setValue(user, Constants.FIELD_SALT,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_SALT));
    ReflectionUtils.setValue(user, Constants.FIELD_FIRST_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_FIRST_NAME));
    ReflectionUtils.setValue(user, Constants.FIELD_LAST_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_LAST_NAME));
    ReflectionUtils.setValue(user, Constants.FIELD_DATE_OF_BIRTH,
        getLocalDate(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_DATE_OF_BIRTH));
    ReflectionUtils.setValue(user, Constants.FIELD_GENDER,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_GENDER));
    ReflectionUtils.setValue(user, Constants.FIELD_MOBILE,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_MOBILE));
    ReflectionUtils.setValue(user, Constants.FIELD_EMAIL,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_EMAIL));
    ReflectionUtils.setValue(user, Constants.FIELD_LOCALE,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_LOCALE));
    ReflectionUtils.setValue(user, Constants.FIELD_PERMISSIONS,
        getEnumList(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS, Permission.class));
  }

  public void save(User user) {

  }
}
