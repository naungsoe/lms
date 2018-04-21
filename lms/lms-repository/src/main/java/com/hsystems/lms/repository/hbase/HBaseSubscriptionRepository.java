package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.SubscriptionRepository;
import com.hsystems.lms.repository.entity.Subscription;
import com.hsystems.lms.repository.hbase.mapper.HBaseSubscriptionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseSubscriptionRepository extends HBaseAbstractRepository
    implements SubscriptionRepository {

  private final HBaseClient client;

  private final HBaseSubscriptionMapper subscriptionMapper;

  @Inject
  HBaseSubscriptionRepository(
      HBaseClient client,
      HBaseSubscriptionMapper subscriptionMapper) {

    this.client = client;
    this.subscriptionMapper = subscriptionMapper;
  }

  @Override
  public List<Subscription> findAllBy(String userId)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(userId);
    Scan scan = getRowKeyFilterScan(userId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Subscription.class);
    List<Result> results = client.scan(scan, tableName);
    List<Subscription> subscriptions
        = subscriptionMapper.getEntities(results);

    return subscriptions;
  }

  @Override
  public Optional<Subscription> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Subscription.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return subscriptionMapper.getEntity(results);
  }

  @Override
  public void create(Subscription entity)
      throws IOException {

  }

  @Override
  public void update(Subscription entity)
      throws IOException {

  }

  @Override
  public void delete(Subscription entity)
      throws IOException {

  }
}