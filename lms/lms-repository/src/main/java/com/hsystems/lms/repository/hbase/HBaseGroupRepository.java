package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.hbase.mapper.HBaseGroupMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository
    extends HBaseRepository implements GroupRepository {

  private final HBaseClient client;

  private final HBaseGroupMapper groupMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseGroupRepository(
      HBaseClient client,
      HBaseGroupMapper groupMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.groupMapper = groupMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<Group> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.GROUP);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<Group> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Question.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Group group = groupMapper.getEntity(results);
    return Optional.of(group);
  }

  @Override
  public void save(Group entity)
      throws IOException {

  }

  @Override
  public void delete(Group entity)
      throws IOException {

  }
}
