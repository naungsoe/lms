package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.ResourcePermission;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.User;

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
public class HBaseShareLogMapper extends HBaseAbstractMapper<ShareLog> {

  @Override
  public List<ShareLog> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<ShareLog> logs = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<ShareLog> logOptional = getEntity(result, results);

      if (logOptional.isPresent()) {
        logs.add(logOptional.get());
      }
    });

    return logs;
  }

  private Optional<ShareLog> getEntity(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    User sharedBy = new User.Builder(
        getId(mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    ).build();
    LocalDateTime sharedDateTime = getDateTime(mainResult);

    List<ResourcePermission> permissions = new ArrayList<>();
    results.stream().filter(isPermissionResult(id))
        .forEach(entryResult -> {
          ResourcePermission permission
              = getResourcePermission(entryResult);
          permissions.add(permission);
        });

    ShareLog log = new ShareLog(
        id,
        permissions,
        sharedBy,
        sharedDateTime
    );
    return Optional.of(log);
  }

  @Override
  public Optional<ShareLog> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}