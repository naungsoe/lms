package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.model.Action;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.ShareLog;
import com.hsystems.lms.model.User;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseShareLogRepository
    extends HBaseRepository implements ShareLogRepository {

  private HBaseClient client;

  @Inject
  HBaseShareLogRepository(HBaseClient client) {
    this.client = client;
  }

  public List<ShareLog> findAllBy(String id)
      throws RepositoryException {

    try {
      Scan scan = getRowFilterScan(id);
      List<Result> results = client.scan(scan, Constants.TABLE_USERS);

      if (CollectionUtils.isEmpty(results)) {
        return Collections.EMPTY_LIST;
      }

      List<ShareLog> shareLogs = new ArrayList<>();

      for (Result result : results) {
        String rowKey = Bytes.toString(result.getRow());

        if (rowKey.contains(Constants.SEPARATOR_USER)) {
          shareLogs.add(getShareLog(rowKey, result));
        }
      }

      return shareLogs;

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving sharelog", e);
    }
  }

  protected ShareLog getShareLog(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    ShareLog shareLog = (ShareLog) ReflectionUtils.getInstance(ShareLog.class);
    User user = (User) ReflectionUtils.getInstance(User.class);
    String id = rowKey.split(Constants.SEPARATOR_SCHOOL)[1];

    ReflectionUtils.setValue(user, Constants.FIELD_ID, id);
    ReflectionUtils.setValue(user, Constants.FIELD_NAME,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_NAME));
    ReflectionUtils.setValue(shareLog, Constants.FIELD_USER, user);

    ReflectionUtils.setValue(shareLog, Constants.FIELD_PERMISSIONS,
        getEnumList(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_PERMISSIONS, Permission.class));

    return shareLog;
  }
}
