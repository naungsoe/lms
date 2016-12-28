package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.EntityType;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.ShareLogEntry;
import com.hsystems.lms.repository.entity.User;

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
  public ShareLog map(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String entityId = Bytes.toString(mainResult.getRow());
    EntityType entityType = getType(mainResult, EntityType.class);

    User sharedBy = new User(
        getId(mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    );
    LocalDateTime sharedDateTime = getDateTime(mainResult);

    List<ShareLogEntry> entries = new ArrayList<>();
    results.stream().filter(isShareResult(entityId))
        .forEach(x -> entries.add(getShareLogEntry(x)));

    return new ShareLog(
        entityId,
        entityType,
        sharedBy,
        sharedDateTime,
        entries
    );
  }

  @Override
  public List<Put> map(ShareLog entity, long timestamp) {
    return null;
  }
}
