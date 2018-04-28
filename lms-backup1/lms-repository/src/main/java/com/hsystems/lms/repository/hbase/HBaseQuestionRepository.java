package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.patch.Patch;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.ShareLog;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.question.QuestionResource;
import com.hsystems.lms.repository.hbase.mapper.HBasePatchMapper;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuestionRepository extends HBaseAbstractRepository
    implements QuestionRepository {

  private final HBaseClient client;

  private final HBasePatchMapper patchMapper;

  private final HBaseQuestionMapper questionMapper;

  private final ShareLogRepository shareLogRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client,
      HBasePatchMapper patchMapper,
      HBaseQuestionMapper questionMapper,
      ShareLogRepository shareLogRepository) {

    this.client = client;
    this.patchMapper = patchMapper;
    this.questionMapper = questionMapper;
    this.shareLogRepository = shareLogRepository;
  }

  @Override
  public List<QuestionResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(QuestionResource.class);
    List<Result> results = client.scan(scan, tableName);
    List<QuestionResource> resources = questionMapper.getEntities(results);

    for (QuestionResource resource : resources) {
      populateShareLog(resource);
    }

    return resources;
  }

  private void populateShareLog(QuestionResource resource)
      throws IOException {

    Optional<ShareLog> logOptional
        = shareLogRepository.findBy(resource.getId());

    if (logOptional.isPresent()) {
      ShareLog shareLog = logOptional.get();
      populatePermissions(resource, shareLog);
    }
  }

  @Override
  public void executeUpdate(Patch patch, User modifiedBy)
      throws IOException {

    TableName tableName = getTableName(QuestionResource.class);
    List<Delete> deletes = patchMapper.getDeletes(patch);

    if (CollectionUtils.isNotEmpty(deletes)) {
      client.delete(deletes, tableName);
    }

    List<Put> puts = patchMapper.getSavePuts(
        patch, modifiedBy, QuestionResource.class);
    client.put(puts, tableName);
  }

  @Override
  public Optional<QuestionResource> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(QuestionResource.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Optional<QuestionResource> resourceOptional
        = questionMapper.getEntity(results);

    if (resourceOptional.isPresent()) {
      QuestionResource resource = resourceOptional.get();
      populateShareLog(resource);
    }

    return resourceOptional;
  }

  @Override
  public void create(QuestionResource entity)
      throws IOException {

    TableName tableName = getTableName(QuestionResource.class);
    List<Put> puts = questionMapper.getSavePuts(entity);
    client.put(puts, tableName);
  }

  @Override
  public void update(QuestionResource entity)
      throws IOException {

    TableName tableName = getTableName(QuestionResource.class);
    List<Put> puts = questionMapper.getSavePuts(entity);

    String startRowKey = entity.getId();
    Scan scan = getRowKeyOnlyFilterScan(startRowKey);
    scan.setStartRow(Bytes.toBytes(startRowKey));

    List<Result> results = client.scan(scan, tableName);
    List<String> unusedRowKeys = getUnusedRowKeys(results, puts);
    List<Delete> deletes = getDeletes(unusedRowKeys);
    client.delete(deletes, tableName);
    client.put(puts, tableName);
  }

  @Override
  public void delete(QuestionResource entity)
      throws IOException {

    TableName tableName = getTableName(QuestionResource.class);
    List<Delete> deletes = questionMapper.getDeletes(entity);
    client.delete(deletes, tableName);
  }
}