package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.SubscriptionRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.Subscription;
import com.hsystems.lms.repository.hbase.mapper.HBaseSubscriptionMapper;
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
public class HBaseSubscriptionRepository extends HBaseAbstractRepository
    implements SubscriptionRepository {

  private final HBaseClient client;

  private final HBaseSubscriptionMapper subscriptionMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseSubscriptionRepository(
      HBaseClient client,
      HBaseSubscriptionMapper subscriptionMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.subscriptionMapper = subscriptionMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<Subscription> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.SUBSCRIPTION);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<Subscription> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, Subscription.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return subscriptionMapper.getEntity(results);
  }

  @Override
  public List<Subscription> findAllBy(String userId)
      throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        userId, userId, Integer.MAX_VALUE, EntityType.SUBSCRIPTION);

    if (CollectionUtils.isEmpty(mutations)) {
      return Collections.emptyList();
    }

    Mutation startMutation = mutations.get(0);
    Mutation stopMutation = mutations.get(mutations.size() - 1);
    String startRowKey = startMutation.getId();
    String stopRowKey = getInclusiveStopRowKey(stopMutation.getId());
    Scan scan = getRowKeyFilterScan(userId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setStopRow(Bytes.toBytes(stopRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = client.scan(scan, Subscription.class);
    List<Subscription> subscriptions
        = subscriptionMapper.getEntities(results, mutations);

    return subscriptions;
  }

  @Override
  public void save(Subscription entity)
      throws IOException {

  }

  @Override
  public void delete(Subscription entity)
      throws IOException {

  }
}