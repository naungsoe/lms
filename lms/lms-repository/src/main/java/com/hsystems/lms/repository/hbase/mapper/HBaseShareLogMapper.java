package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.ResourcePermission;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseShareLogMapper extends HBaseMapper<ShareLog> {

  @Override
  public List<ShareLog> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<ShareLog> shareLogs = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        ShareLog shareLog = getEntity(result, results, timestamp);
        shareLogs.add(shareLog);
      }
    });
    return shareLogs;
  }

  private ShareLog getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    User sharedBy = new User(
        getId(mainResult, timestamp),
        getFirstName(mainResult, timestamp),
        getLastName(mainResult, timestamp)
    );
    LocalDateTime sharedDateTime = getDateTime(mainResult, timestamp);

    List<ResourcePermission> permissions = new ArrayList<>();
    results.stream().filter(isPermissionResult(id))
        .forEach(permissionResult -> {
          ResourcePermission permission
              = getResourcePermission(permissionResult, timestamp);
          permissions.add(permission);
        });

    return new ShareLog(
        id,
        sharedBy,
        sharedDateTime,
        permissions
    );
  }

  @Override
  public ShareLog getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(ShareLog entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(ShareLog entity, long timestamp) {
    return Collections.emptyList();
  }
}
