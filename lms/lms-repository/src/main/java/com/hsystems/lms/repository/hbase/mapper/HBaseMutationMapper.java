package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseMutationMapper extends HBaseAbstractMapper<Mutation> {

  private static final String PATTERN_KEY = "^([A-Z]*)_([a-z0-9]*)$";

  private static final String FORMAT_KEY = "%s_%s";

  public String getId(EntityType type, String id) {
    return String.format(FORMAT_KEY, type, id);
  }

  @Override
  public List<Mutation> getEntities(
      List<Result> results, List<Mutation> list) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Mutation> mutations = new ArrayList<>();
    results.forEach(result -> {
      Optional<Mutation> mutationOptional = getEntity(result);

      if (mutationOptional.isPresent()) {
        mutations.add(mutationOptional.get());
      }
    });
    return mutations;
  }

  public Optional<Mutation> getEntity(Result result) {
    String rowKey = Bytes.toString(result.getRow());
    Pattern pattern = Pattern.compile(PATTERN_KEY);
    Matcher matcher = pattern.matcher(rowKey);

    if (!matcher.matches()) {
      return Optional.empty();
    }

    String id = matcher.group(2);
    EntityType entityType = EntityType.valueOf(matcher.group(1));
    ActionType actionType = getActionType(result, 0);
    long timestamp = getTimestamp(result, 0);

    Mutation mutation = new Mutation(
        id,
        entityType,
        actionType,
        timestamp
    );
    return Optional.of(mutation);
  }

  @Override
  public Optional<Mutation> getEntity(List<Result> results) {
    Result mainResult = results.get(0);
    return getEntity(mainResult);
  }

  @Override
  public List<Put> getPuts(Mutation entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    String id = String.format(FORMAT_KEY,
        entity.getEntityType(), entity.getId());
    byte[] rowKey = Bytes.toBytes(id);
    Put logPut = new Put(rowKey, timestamp);
    addEntityTypeColumn(logPut, entity.getEntityType());
    addActionTypeColumn(logPut, entity.getActionType());
    addTimestampColumn(logPut, timestamp);
    puts.add(logPut);
    return puts;
  }

  @Override
  public List<Delete> getDeletes(Mutation entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();
    String id = String.format(FORMAT_KEY,
        entity.getEntityType(), entity.getId());
    byte[] rowKey = Bytes.toBytes(id);
    Delete delete = new Delete(rowKey, timestamp);
    deletes.add(delete);
    return deletes;
  }
}
