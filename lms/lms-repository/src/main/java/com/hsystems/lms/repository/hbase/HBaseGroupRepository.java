package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.entity.Question;
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

  private final HBaseGroupMapper groupMapper;

  private final MutateLogRepository mutateLogRepository;

  @Inject
  HBaseGroupRepository(
      HBaseClient client,
      HBaseGroupMapper groupMapper,
      MutateLogRepository mutateLogRepository) {

    this.client = client;
    this.groupMapper = groupMapper;
    this.mutateLogRepository = mutateLogRepository;
  }

  @Override
  public Optional<Group> findBy(String id)
      throws IOException {

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(id, EntityType.GROUP);

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(mutateLog.getTimestamp());

    List<Result> results = client.scan(scan, Question.class);

    if (results.isEmpty()) {
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
