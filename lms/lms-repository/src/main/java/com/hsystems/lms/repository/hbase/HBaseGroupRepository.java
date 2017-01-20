package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.hbase.mapper.HBaseGroupMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository
    extends HBaseRepository implements GroupRepository {

  private final HBaseClient client;

  private final HBaseGroupMapper mapper;

  @Inject
  HBaseGroupRepository(
      HBaseClient client,
      HBaseGroupMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<Group> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan,
        Constants.TABLE_GROUPS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Group group = mapper.getEntity(results);
    return Optional.of(group);
  }

  @Override
  public void save(Group entity, long timestamp)
      throws IOException {

  }

  @Override
  public void delete(Group entity, long timestamp)
      throws IOException {

  }
}
