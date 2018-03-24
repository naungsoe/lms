package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.quiz.QuizResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuizMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuizRepository extends HBaseAbstractRepository
    implements QuizRepository {

  private final HBaseClient client;

  private final HBaseQuizMapper quizMapper;

  private final ShareLogRepository shareLogRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient client,
      HBaseQuizMapper quizMapper,
      ShareLogRepository shareLogRepository) {

    this.client = client;
    this.quizMapper = quizMapper;
    this.shareLogRepository = shareLogRepository;
  }

  @Override
  public Optional<QuizResource> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(QuizResource.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Optional<QuizResource> resourceOptional
        = quizMapper.getEntity(results);

    if (resourceOptional.isPresent()) {
      QuizResource resource = resourceOptional.get();
      populateShareLog(resource);
    }

    return resourceOptional;
  }

  private void populateShareLog(QuizResource resource)
      throws IOException {

    Optional<ShareLog> logOptional
        = shareLogRepository.findBy(resource.getId());

    if (logOptional.isPresent()) {
      ShareLog shareLog = logOptional.get();
      populatePermissionSets(resource, shareLog);
    }
  }

  @Override
  public List<QuizResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(QuizResource.class);
    List<Result> results = client.scan(scan, tableName);
    List<QuizResource> resources = quizMapper.getEntities(results);

    for (QuizResource resource : resources) {
      populateShareLog(resource);
    }

    return resources;
  }

  @Override
  public void save(QuizResource entity)
      throws IOException {

  }

  @Override
  public void delete(QuizResource entity)
      throws IOException {

  }
}