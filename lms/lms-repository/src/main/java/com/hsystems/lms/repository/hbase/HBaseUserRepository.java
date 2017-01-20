package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseUserMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  private final HBaseClient client;

  private final HBaseUserMapper mapper;

  @Inject
  HBaseUserRepository(
      HBaseClient client,
      HBaseUserMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<User> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Constants.TABLE_USERS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    User user = mapper.getEntity(results);
    return Optional.of(user);
  }

  @Override
  public void save(User entity, long timestamp)
      throws IOException {

  }

  @Override
  public void delete(User entity, long timestamp)
      throws IOException {

  }
}
