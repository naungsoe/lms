package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseGroupMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository extends HBaseAbstractRepository
    implements GroupRepository {

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

    List<Result> results = client.scan(scan, QuestionResource.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return groupMapper.getEntity(results);
  }

  @Override
  public List<Group> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.GROUP);

    if (CollectionUtils.isEmpty(mutations)) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startMutation.getId()));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, Group.class);
    return groupMapper.getEntities(results, mutations);
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
