package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.ShareLogEntry;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseShareLogMapper extends HBaseMapper<ShareLog> {

  @Override
  public ShareLog getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    EntityType type = getType(mainResult, EntityType.class);
    User sharedBy = new User(
        getId(mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    );
    LocalDateTime sharedDateTime = getDateTime(mainResult);

    List<ShareLogEntry> logEntries = new ArrayList<>();
    results.stream().filter(isShareResult(id))
        .forEach(shareResult -> {
          ShareLogEntry logEntry = getShareLogEntry(shareResult);
          logEntries.add(logEntry);
        });

    return new ShareLog(
        id,
        type,
        sharedBy,
        sharedDateTime,
        logEntries
    );
  }

  @Override
  public List<Put> getPuts(ShareLog entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(ShareLog entity, long timestamp) {
    return new ArrayList<>();
  }
}