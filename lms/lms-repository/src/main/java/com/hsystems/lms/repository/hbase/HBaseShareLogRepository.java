package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.hbase.mapper.HBaseShareLogMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseShareLogRepository
    extends HBaseRepository implements ShareLogRepository {

  private final HBaseClient client;

  private final HBaseShareLogMapper mapper;

  @Inject
  HBaseShareLogRepository(
      HBaseClient client,
      HBaseShareLogMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<ShareLog> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan,
        Constants.TABLE_SHARE_LOGS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    ShareLog shareLog = mapper.getEntity(results);
    return Optional.of(shareLog);
  }

  @Override
  public void save(ShareLog entity, long timestamp)
      throws IOException {

  }

  @Override
  public void delete(ShareLog entity, long timestamp)
      throws IOException {

  }
}
