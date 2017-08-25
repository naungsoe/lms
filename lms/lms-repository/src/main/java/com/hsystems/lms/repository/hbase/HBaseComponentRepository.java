package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.hbase.mapper.HBaseComponentMapper;
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
public class HBaseComponentRepository extends HBaseRepository
    implements ComponentRepository {

  private final HBaseClient client;

  private final HBaseComponentMapper componentMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseComponentRepository(
      HBaseClient client,
      HBaseComponentMapper componentMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.componentMapper = componentMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<Component> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.QUESTION);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<Component> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Component.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Component component = componentMapper.getEntity(results);
    return Optional.of(component);
  }

  @Override
  public List<Component> findAllBy(String schoolId, String resourceId)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, resourceId, Integer.MAX_VALUE, EntityType.COMPONENT);

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

    List<Result> results = client.scan(scan, Component.class);
    return componentMapper.getEntities(results, mutations);
  }

  @Override
  public void save(Component entity)
      throws IOException {

  }

  @Override
  public void delete(Component entity)
      throws IOException {

  }
}
