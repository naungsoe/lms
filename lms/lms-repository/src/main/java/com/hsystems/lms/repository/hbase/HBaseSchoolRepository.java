package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.hbase.mapper.HBaseSchoolMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseSchoolRepository
    extends HBaseRepository implements SchoolRepository {

  private final HBaseClient client;

  private final HBaseSchoolMapper schoolMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseSchoolRepository(
      HBaseClient client,
      HBaseSchoolMapper schoolMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.schoolMapper = schoolMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<School> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.SCHOOL);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    return findBy(id, mutation.getTimestamp());
  }

  private Optional<School> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan, School.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    School school = schoolMapper.getEntity(results);
    return Optional.of(school);
  }

  @Override
  public void save(School entity)
      throws IOException {

  }

  @Override
  public void delete(School entity)
      throws IOException {

  }
}
