package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.MutateLog;
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

  private final HBaseUserMapper userMapper;

  private final MutateLogRepository mutateLogRepository;

  @Inject
  HBaseUserRepository(
      HBaseClient client,
      HBaseUserMapper userMapper,
      MutateLogRepository mutateLogRepository) {

    this.client = client;
    this.userMapper = userMapper;
    this.mutateLogRepository = mutateLogRepository;
  }

  @Override
  public Optional<User> findBy(String id)
      throws IOException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, EntityType.USER);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(mutateLog.getTimestamp());

    List<Result> results = client.scan(scan, User.class);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    User user = userMapper.getEntity(results);
    return Optional.of(user);
  }

  @Override
  public void save(User entity)
      throws IOException {

  }

  @Override
  public void delete(User entity)
      throws IOException {

  }
}
