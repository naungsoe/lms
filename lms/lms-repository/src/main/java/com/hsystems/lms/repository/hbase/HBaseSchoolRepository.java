package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.repository.entity.School;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseSchoolRepository
    extends HBaseRepository implements SchoolRepository {

  private HBaseClient client;

  @Inject
  HBaseSchoolRepository(HBaseClient client) {
    this.client = client;
  }

  public Optional<School> findBy(String id)
      throws RepositoryException {

    try {
      Get get = new Get(Bytes.toBytes(id));
      Result result = client.get(get, Constants.TABLE_SCHOOLS);

      if (result.isEmpty()) {
        return Optional.empty();
      }

      School school = (School) ReflectionUtils.getInstance(School.class);
      ReflectionUtils.setValue(
          school, Constants.FIELD_ID, Bytes.toString(result.getRow()));
      populateSchoolFields(school, result);
      return Optional.of(school);

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving school", e);
    }
  }

  protected void populateSchoolFields(School school, Result result)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    ReflectionUtils.setValue(school, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    ReflectionUtils.setValue(school, Constants.FIELD_LOCALE,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_LOCALE));
    ReflectionUtils.setValue(school, Constants.FIELD_PERMISSIONS,
        getEnumList(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS, Permission.class));
  }
}
