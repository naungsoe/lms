package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Constants;
import com.hsystems.lms.model.Group;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.School;
import com.hsystems.lms.model.User;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  public Optional<User> findBy(String id)
      throws RepositoryException {

    Configuration configuration = HBaseConfiguration.create();
    String keyRegex = String.format("^%s[_0-9a-zA-Z]*$", id);
    Scan scan = new Scan(Bytes.toBytes(id));
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    scan.setFilter(filter);

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      TableName tableName = TableName.valueOf(Constants.TABLE_USERS);
      Table table = connection.getTable(tableName);
      ResultScanner scanner = table.getScanner(scan);
      return getUser(scanner);
    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {
      throw new RepositoryException(
          "error retrieving user", e);
    }
  }

  protected Optional<User> getUser(ResultScanner scanner)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Iterator<Result> iterator = scanner.iterator();

    if (!iterator.hasNext()) {
      return Optional.empty();
    }

    User user = (User) ReflectionUtils.getInstance(User.class);
    List<Group> groups = new ArrayList<>();

    while (iterator.hasNext()) {
      Result result = iterator.next();
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

  protected School getSchool(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    School school = (School) ReflectionUtils.getInstance(School.class);
    String id = rowKey.split(Constants.SEPARATOR_SCHOOL)[1];
    ReflectionUtils.setValue(school, Constants.FIELD_ID, id);

    ReflectionUtils.setValue(school, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    ReflectionUtils.setValue(school, Constants.FIELD_LOCALE,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_LOCALE));
    return school;
  }

  protected Group getGroup(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Group group = (Group) ReflectionUtils.getInstance(Group.class);
    String id = rowKey.split(Constants.SEPARATOR_GROUP)[1];
    ReflectionUtils.setValue(group, Constants.FIELD_ID, id);

    ReflectionUtils.setValue(group, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    return group;
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
