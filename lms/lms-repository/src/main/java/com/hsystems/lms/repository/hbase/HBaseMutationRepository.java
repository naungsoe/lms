package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.hbase.mapper.HBaseMutationMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseMutationRepository
    extends HBaseRepository implements MutationRepository {

  private final HBaseClient client;

  private final HBaseMutationMapper mutationMapper;

  @Inject
  HBaseMutationRepository(
      HBaseClient client,
      HBaseMutationMapper mutationMapper) {

    this.client = client;
    this.mutationMapper = mutationMapper;
  }

  @Override
  public Optional<Mutation> findBy(String id, EntityType type)
      throws IOException {

    String logId = mutationMapper.getId(type, id);
    return findBy(logId);
  }

  @Override
  public Optional<Mutation> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, Mutation.class);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    Mutation mutation = mutationMapper.getEntity(Arrays.asList(result));
    return Optional.of(mutation);
  }

  @Override
  public List<Mutation> findAllBy(
      String schoolId, String lastId, int limit, EntityType type)
      throws IOException {

    String prefix = mutationMapper.getId(type, schoolId);
    String startRowKey = mutationMapper.getId(type, lastId);
    startRowKey = getExclusiveStartRowKey(startRowKey);

    Scan scan = getRowKeyFilterScan(prefix);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setCaching(limit);

    List<Result> results = client.scan(scan, Mutation.class);
    return mutationMapper.getEntities(results, Collections.emptyList());
  }

  @Override
  public void save(Mutation mutation)
      throws IOException {

    List<Put> puts = mutationMapper.getPuts(
        mutation, mutation.getTimestamp());
    client.put(puts, Mutation.class);
  }

  @Override
  public void delete(Mutation auditLog)
      throws IOException {

  }
}
