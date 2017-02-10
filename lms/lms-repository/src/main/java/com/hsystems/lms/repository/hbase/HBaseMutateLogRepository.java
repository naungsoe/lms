package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseMutateLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseMutateLogRepository
    extends HBaseRepository implements MutateLogRepository {

  private final HBaseClient client;

  private final HBaseMutateLogMapper mapper;

  @Inject
  HBaseMutateLogRepository(
      HBaseClient client,
      HBaseMutateLogMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<MutateLog> findBy(String id)
      throws IOException {

    return findBy(id, Long.MIN_VALUE);
  }

  @Override
  public Optional<MutateLog> findBy(String id, long timestamp)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));

    if (timestamp != Long.MIN_VALUE) {
      get.setTimeStamp(timestamp);
    }

    Result result = client.get(get, Constants.TABLE_MUTATE_LOGS);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mapper.getEntity(Arrays.asList(result));
    return Optional.of(mutateLog);
  }

  @Override
  public void save(MutateLog auditLog, long timestamp)
      throws IOException {

    List<Put> puts = mapper.getPuts(auditLog, timestamp);
    client.put(puts, Constants.TABLE_MUTATE_LOGS);
  }

  @Override
  public void delete(MutateLog auditLog, long timestamp)
      throws IOException {

    List<Delete> deletes = mapper.getDeletes(auditLog, timestamp);
    client.delete(deletes, Constants.TABLE_MUTATE_LOGS);
  }
}
