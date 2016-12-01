package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.common.Permission;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository
    extends HBaseRepository implements GroupRepository {

  private final HBaseClient client;

  @Inject
  public HBaseGroupRepository(HBaseClient client) {
    this.client = client;
  }

  public Optional<Group> findBy(String id)
      throws RepositoryException {

    try {
      Get get = new Get(Bytes.toBytes(id));
      Result result = client.get(get, Constants.TABLE_GROUPS);

      if (result.isEmpty()) {
        return Optional.empty();
      }

      Group group = (Group) ReflectionUtils.getInstance(Group.class);
      ReflectionUtils.setValue(
          group, Constants.FIELD_ID, Bytes.toString(result.getRow()));
      populateGroupFields(group, result);
      return Optional.of(group);

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving group", e);
    }
  }

  protected void populateGroupFields(Group group, Result result)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    ReflectionUtils.setValue(group, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    ReflectionUtils.setValue(group, Constants.FIELD_PERMISSIONS,
        getEnumList(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS, Permission.class));
  }
}
