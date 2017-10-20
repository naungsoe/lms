package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.UserEnrollmentRepository;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.UserEnrollment;
import com.hsystems.lms.repository.hbase.mapper.HBaseUserEnrollmentMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HBaseUserEnrollmentRepository extends HBaseAbstractRepository
    implements UserEnrollmentRepository {

  private final HBaseClient client;

  private final HBaseUserEnrollmentMapper enrollmentMapper;

  @Inject
  HBaseUserEnrollmentRepository(
      HBaseClient client,
      HBaseUserEnrollmentMapper enrollmentMapper) {

    this.client = client;
    this.enrollmentMapper = enrollmentMapper;
  }

  @Override
  public Optional<UserEnrollment> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));

    List<Result> results = client.scan(scan, User.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return enrollmentMapper.getEntity(results);
  }

  @Override
  public List<UserEnrollment> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setCaching(limit);

    List<Result> results = client.scan(scan, UserEnrollment.class);

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Mutation> mutations = Collections.emptyList();
    return enrollmentMapper.getEntities(results, mutations);
  }

  @Override
  public List<UserEnrollment> findAllBy(String schoolId, String hostId)
      throws IOException {

    Scan scan = getRowKeyFilterScan(schoolId, hostId);
    scan.setStartRow(Bytes.toBytes(schoolId));

    List<Result> results = client.scan(scan, UserEnrollment.class);

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<String> rowKeys = new ArrayList<>();
    results.forEach(result -> {
      String rowKey = Bytes.toString(result.getRow());
      rowKeys.add(rowKey);
    });
    rowKeys.sort(Comparator.naturalOrder());

    String startRowKey = rowKeys.get(0);
    String stopRowKey = rowKeys.get(rowKeys.size() - 1);
    stopRowKey = getInclusiveStopRowKey(stopRowKey);
    scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setStopRow(Bytes.toBytes(stopRowKey));

    results = client.scan(scan, UserEnrollment.class);

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Mutation> mutations = Collections.emptyList();
    return enrollmentMapper.getEntities(results, mutations);
  }

  @Override
  public void save(UserEnrollment entity)
      throws IOException {

  }

  @Override
  public void delete(UserEnrollment entity)
      throws IOException {

  }
}