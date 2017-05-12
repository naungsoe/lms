package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.hbase.mapper.HBaseUserMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserRepository
    extends HBaseRepository implements UserRepository {

  private final HBaseClient client;

  private final HBaseUserMapper userMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseUserRepository(
      HBaseClient client,
      HBaseUserMapper userMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.userMapper = userMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<User> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.USER);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(mutation.getTimestamp());

    List<Result> results = client.scan(scan, User.class);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    User user = userMapper.getEntity(results);
    return Optional.of(user);
  }

  @Override
  public List<User> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.USER);

    if (mutations.isEmpty()) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startMutation.getId()));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, User.class);
    return userMapper.getEntities(results, mutations);
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
