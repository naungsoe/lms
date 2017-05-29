package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.hbase.mapper.HBaseSubjectMapper;
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
public class HBaseSubjectRepository
    extends HBaseRepository implements SubjectRepository {

  private final HBaseClient client;

  private final HBaseSubjectMapper subjectMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseSubjectRepository(
      HBaseClient client,
      HBaseSubjectMapper subjectMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.subjectMapper = subjectMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<Subject> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.SUBJECT);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<Subject> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, School.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Subject subject = subjectMapper.getEntity(results);
    return Optional.of(subject);
  }

  @Override
  public List<Subject> findAllBy(String schoolId)
    throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, schoolId, Integer.MAX_VALUE, EntityType.SUBJECT);

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

    List<Result> results = client.scan(scan, Subject.class);
    return subjectMapper.getEntities(results, mutations);
  }

  @Override
  public void save(Subject entity)
      throws IOException {

  }

  @Override
  public void delete(Subject entity)
      throws IOException {

  }
}
