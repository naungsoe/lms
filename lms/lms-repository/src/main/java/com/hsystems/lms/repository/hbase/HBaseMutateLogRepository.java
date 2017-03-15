package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseMutateLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseMutateLogRepository
    extends HBaseRepository implements MutateLogRepository {

  private final HBaseClient client;

  private final HBaseMutateLogMapper mutateLogMapper;

  @Inject
  HBaseMutateLogRepository(
      HBaseClient client,
      HBaseMutateLogMapper mutateLogMapper) {

    this.client = client;
    this.mutateLogMapper = mutateLogMapper;
  }

  @Override
  public Optional<MutateLog> findBy(String id, EntityType type)
      throws IOException {

    String logId = String.format(HBaseMutateLogMapper.KEY_FORMAT, type, id);
    return findBy(logId);
  }

  @Override
  public Optional<MutateLog> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, MutateLog.class);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogMapper.getEntity(result);
    return Optional.of(mutateLog);
  }

  @Override
  public void save(MutateLog mutateLog)
      throws IOException {

    List<Put> puts = mutateLogMapper.getPuts(
        mutateLog, mutateLog.getTimestamp());
    client.put(puts, MutateLog.class);
  }

  @Override
  public void delete(MutateLog auditLog)
      throws IOException {

  }
}
