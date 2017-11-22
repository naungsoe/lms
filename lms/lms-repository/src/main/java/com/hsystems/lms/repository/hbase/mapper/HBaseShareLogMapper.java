package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.PermissionSet;
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
public class HBaseShareLogMapper extends HBaseAbstractMapper<ShareLog> {

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
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<ShareLog> shareLogOptional
            = getEntity(result, results, timestamp);

        if (shareLogOptional.isPresent()) {
          shareLogs.add(shareLogOptional.get());
        }
      }
    });
    return shareLogs;
  }

  private Optional<ShareLog> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    User sharedBy = new User.Builder(
        getId(mainResult, timestamp),
        getFirstName(mainResult, timestamp),
        getLastName(mainResult, timestamp)
    ).build();
    LocalDateTime sharedDateTime = getDateTime(mainResult, timestamp);

    List<PermissionSet> shareEntries = new ArrayList<>();
    results.stream().filter(isEntryResult(id))
        .forEach(entryResult -> {
          PermissionSet permissionSet
              = getPermissionSet(entryResult, timestamp);
          shareEntries.add(permissionSet);
        });

    ShareLog shareLog = new ShareLog(
        id,
        shareEntries,
        sharedBy,
        sharedDateTime
    );
    return Optional.of(shareLog);
  }

  @Override
  public Optional<ShareLog> getEntity(List<Result> results) {
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
