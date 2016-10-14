package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Constants;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.School;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
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
 * Created by administrator on 12/10/16.
 */
public class HBaseSchoolRepository
    extends HBaseRepository implements SchoolRepository {

  private Provider<HBaseClient> hBaseClientProvider;

  @Inject
  HBaseSchoolRepository(Provider<HBaseClient> hBaseClientProvider) {
    this.hBaseClientProvider = hBaseClientProvider;
  }

  public Optional<School> findBy(String key)
      throws RepositoryException {

    HBaseClient client = hBaseClientProvider.get();
    Get get = new Get(Bytes.toBytes(key));

    try {
      Optional<Result> result = client.getResult(get, Constants.TABLE_SCHOOLS);

      if (result.isPresent()) {
        School school = (School) ReflectionUtils.getInstance(School.class);
        ReflectionUtils.setValue(
            school, Constants.FIELD_ID, Bytes.toString(result.get().getRow()));
        populateSchoolFields(school, result.get());
        return Optional.of(school);
      }
      return Optional.empty();
    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {
      throw new RepositoryException(
          "error retrieving school", e);
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
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS));
  }
}
