package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.DateUtils;
import com.hsystems.lms.common.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.model.Group;
import com.hsystems.lms.repository.model.School;
import com.hsystems.lms.repository.model.User;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  protected Scan getRowFilterScan(String key) {
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

    return Bytes.toString(result.getValue(family, identifier));
  }

  protected LocalDate getLocalDate(
      Result result, byte[] family, byte[] identifier) {

    String date = Bytes.toString(result.getValue(family, identifier));
    return DateUtils.toLocalDate(date, Constants.DATE_FORMAT);
  }

  protected LocalDate getLocalDateTime(
      Result result, byte[] family, byte[] identifier) {

    String dateTime = Bytes.toString(result.getValue(family, identifier));
    return DateUtils.toLocalDate(dateTime, Constants.DATE_TIME_FORMAT);
  }

  protected <E extends Enum<E>> E getEnum(
      Result result, byte[] family, byte[] identifier, Class<E> type)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    String value = Bytes.toString(result.getValue(family, identifier));
    return Enum.valueOf(type, value);
  }

  protected <E extends Enum<E>> List<E> getEnumList(
      Result result, byte[] family, byte[] identifier, Class<E> type)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    List<E> list = new ArrayList<>();
    String[] values = Bytes.toString(result.getValue(
        family, identifier)).split("\\,");

    for (String value : values) {
      list.add(Enum.valueOf(type, value));
    }

    return list;
  }
}
