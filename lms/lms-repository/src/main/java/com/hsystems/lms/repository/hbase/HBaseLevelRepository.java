package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.hbase.mapper.HBaseLevelMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseLevelRepository extends HBaseAbstractRepository
    implements LevelRepository {

  private final HBaseClient client;

  private final HBaseLevelMapper levelMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseLevelRepository(
      HBaseClient client,
      HBaseLevelMapper levelMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.levelMapper = levelMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<Level> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.LEVEL);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(mutation.getTimestamp());

    List<Result> results = client.scan(scan, School.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return levelMapper.getEntity(results);
  }

  @Override
  public List<Level> findAllBy(String schoolId)
    throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, schoolId, Integer.MAX_VALUE, EntityType.LEVEL);

    if (CollectionUtils.isEmpty(mutations)) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String startRowKey = startMutation.getId();
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, Level.class);
    return levelMapper.getEntities(results, mutations);
  }

  @Override
  public void save(Level entity)
      throws IOException {

  }

  @Override
  public void delete(Level entity)
      throws IOException {

  }
}
