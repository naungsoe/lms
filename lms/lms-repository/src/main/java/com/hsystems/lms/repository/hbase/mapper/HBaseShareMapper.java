package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Share;
import com.hsystems.lms.repository.entity.ShareEntry;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseShareMapper extends HBaseMapper<Share> {

  @Override
  public List<Share> getEntities(List<Result> results) {
    if (results.isEmpty()) {
      return Collections.emptyList();
    }

    List<Share> shares = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Share share = getEntity(result, results);
      shares.add(share);
    });
    return shares;
  }

  private Share getEntity(Result mainResult, List<Result> results) {
    String id = Bytes.toString(mainResult.getRow());
    EntityType type = getType(mainResult, EntityType.class);
    User sharedBy = new User(
        getId(mainResult),
        getFirstName(mainResult),
        getLastName(mainResult)
    );
    LocalDateTime sharedDateTime = getDateTime(mainResult);

    List<ShareEntry> logEntries = new ArrayList<>();
    results.stream().filter(isShareResult(id))
        .forEach(shareResult -> {
          ShareEntry logEntry = getShareEntry(shareResult);
          logEntries.add(logEntry);
        });

    return new Share(
        id,
        type,
        sharedBy,
        sharedDateTime,
        logEntries
    );
  }

  @Override
  public Share getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }

  @Override
  public List<Put> getPuts(Share entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Share entity, long timestamp) {
    return new ArrayList<>();
  }
}
