package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
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
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseShareMapper extends HBaseMapper<Share> {

  @Override
  public List<Share> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Share> shares = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        Share share = getEntity(result, results, timestamp);
        shares.add(share);
      }
    });
    return shares;
  }

  private Share getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    EntityType type = getType(mainResult, timestamp, EntityType.class);
    User sharedBy = new User(
        getId(mainResult, timestamp),
        getFirstName(mainResult, timestamp),
        getLastName(mainResult, timestamp)
    );
    LocalDateTime sharedDateTime = getDateTime(mainResult, timestamp);

    List<ShareEntry> logEntries = new ArrayList<>();
    results.stream().filter(isShareResult(id))
        .forEach(shareResult -> {
          ShareEntry logEntry = getShareEntry(shareResult, timestamp);
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
    return getEntity(mainResult, results, 0);
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
