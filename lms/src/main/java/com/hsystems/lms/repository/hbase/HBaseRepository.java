package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.DateUtils;
import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Constants;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.User;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  protected String getString(
      Result result, byte[] family, byte[] identifier) {

    return Bytes.toString(result.getValue(family, identifier));
  }

  protected LocalDate getLocalDate(
      Result result, byte[] family, byte[] identifier) {

    String localDate = Bytes.toString(result.getValue(family, identifier));
    return DateUtils.toLocalDate(localDate);
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
