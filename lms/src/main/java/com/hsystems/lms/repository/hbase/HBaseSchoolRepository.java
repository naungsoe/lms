package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Constants;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.School;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseSchoolRepository
    extends HBaseRepository implements SchoolRepository {

  public Optional<School> findBy(String id)
      throws RepositoryException {

    Configuration configuration = HBaseConfiguration.create();
    Get get = new Get(Bytes.toBytes(id));

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      TableName tableName = TableName.valueOf(Constants.TABLE_SCHOOLS);
      Table table = connection.getTable(tableName);
      Result result = table.get(get);

      if (!result.isEmpty()) {
        School school = (School) ReflectionUtils.getInstance(School.class);
        ReflectionUtils.setValue(
            school, Constants.FIELD_ID, Bytes.toString(result.getRow()));
        populateSchoolFields(school, result);
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
        getEnumList(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS, Permission.class));
  }
}
