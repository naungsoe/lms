package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.entity.lesson.LessonResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseLessonMapper;
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
public class HBaseLessonRepository extends HBaseAbstractRepository
    implements LessonRepository {

  private final HBaseClient client;

  private final HBaseLessonMapper lessonMapper;

  @Inject
  HBaseLessonRepository(
      HBaseClient client,
      HBaseLessonMapper lessonMapper) {

    this.client = client;
    this.lessonMapper = lessonMapper;
  }

  @Override
  public List<LessonResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(LessonResource.class);
    List<Result> results = client.scan(scan, tableName);
    List<LessonResource> resources = lessonMapper.getEntities(results);

    return resources;
  }

  @Override
  public Optional<LessonResource> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(LessonResource.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return lessonMapper.getEntity(results);
  }

  @Override
  public void create(LessonResource entity)
      throws IOException {

  }

  @Override
  public void update(LessonResource entity)
      throws IOException {

  }

  @Override
  public void delete(LessonResource entity)
      throws IOException {

  }
}
