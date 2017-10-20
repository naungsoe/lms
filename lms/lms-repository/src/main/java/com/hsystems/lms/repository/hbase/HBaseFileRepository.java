package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.FileRepository;
import com.hsystems.lms.repository.MutationRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.file.FileResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseFileMapper;
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
public class HBaseFileRepository extends HBaseAbstractRepository
    implements FileRepository {

  private final HBaseClient client;

  private final HBaseFileMapper fileMapper;

  private final MutationRepository mutationRepository;

  @Inject
  HBaseFileRepository(
      HBaseClient client,
      HBaseFileMapper fileMapper,
      MutationRepository mutationRepository) {

    this.client = client;
    this.fileMapper = fileMapper;
    this.mutationRepository = mutationRepository;
  }

  @Override
  public Optional<FileResource> findBy(String id)
      throws IOException {

    Optional<Mutation> mutationOptional
        = mutationRepository.findBy(id, EntityType.FILE);

    if (!mutationOptional.isPresent()) {
      return Optional.empty();
    }

    Mutation mutation = mutationOptional.get();
    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setTimeStamp(mutation.getTimestamp());

    List<Result> results = client.scan(scan, FileResource.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return fileMapper.getEntity(results);
  }

  @Override
  public List<FileResource> findAllBy(
      String schoolId, String lastId, int limit)
    throws IOException {

    List<Mutation> mutations = mutationRepository.findAllBy(
        schoolId, lastId, limit, EntityType.FILE);

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
    return fileMapper.getEntities(results, mutations);
  }

  @Override
  public void save(FileResource entity)
      throws IOException {

  }

  @Override
  public void delete(FileResource entity)
      throws IOException {

  }
}
