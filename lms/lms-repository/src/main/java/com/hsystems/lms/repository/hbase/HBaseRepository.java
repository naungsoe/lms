package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  protected Scan getRowFilterScan(String key)
      throws IOException {

    String keyRegex = String.format("^%s[_0-9a-zA-Z]*$", key);
    Scan scan = new Scan(Bytes.toBytes(key));
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    scan.setFilter(filter);
    return scan;
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

  protected User getUser(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    User user = (User) ReflectionUtils.getInstance(User.class);
    String id = rowKey.split(Constants.SEPARATOR_GROUP)[1];

    ReflectionUtils.setValue(user, Constants.FIELD_ID, id);
    ReflectionUtils.setValue(user, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    return user;
  }

  protected String getString(
      Result result, byte[] family, byte[] identifier) {

    byte[] value = result.getValue(family, identifier);
    return (value == null) ? null : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(
      Result result, byte[] family, byte[] identifier) {

    byte[] value = result.getValue(family, identifier);
    return (value == null) ? null : DateTimeUtils.toLocalDateTime(
        Bytes.toString(value), Constants.DATE_TIME_FORMAT);
  }

  protected <E extends Enum<E>> E getEnum(
      Result result, byte[] family, byte[] identifier, Class<E> type)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    byte[] value = result.getValue(family, identifier);
    return (value == null) ? null : Enum.valueOf(type, Bytes.toString(value));
  }

  protected <E extends Enum<E>> List<E> getEnumList(
      Result result, byte[] family, byte[] identifier, Class<E> type)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    byte[] value = result.getValue(family, identifier);

    if (value == null) {
      return Collections.EMPTY_LIST;
    }

    String[] items = Bytes.toString(value).split("\\,");
    List<E> list = new ArrayList<>();

    for (String item : items) {
      list.add(Enum.valueOf(type, item));
    }

    return list;
  }
}
