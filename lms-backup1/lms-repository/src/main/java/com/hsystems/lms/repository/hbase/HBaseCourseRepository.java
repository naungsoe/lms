package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.CourseRepository;
import com.hsystems.lms.repository.entity.course.CourseResource;
import com.hsystems.lms.repository.hbase.mapper.HBaseCourseMapper;
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
public class HBaseCourseRepository extends HBaseAbstractRepository
    implements CourseRepository {

  private final HBaseClient client;

  private final HBaseCourseMapper courseMapper;

  @Inject
  HBaseCourseRepository(
      HBaseClient client,
      HBaseCourseMapper courseMapper) {

    this.client = client;
    this.courseMapper = courseMapper;
  }

  @Override
  public List<CourseResource> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {

    String startRowKey = getExclusiveStartRowKey(lastId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    TableName tableName = getTableName(CourseResource.class);
    List<Result> results = client.scan(scan, tableName);
    List<CourseResource> courseResources = courseMapper.getEntities(results);

    return courseResources;
  }

  @Override
  public Optional<CourseResource> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(CourseResource.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return courseMapper.getEntity(results);
  }

  @Override
  public void create(CourseResource entity)
      throws IOException {

  }

  @Override
  public void update(CourseResource entity)
      throws IOException {

  }

  @Override
  public void delete(CourseResource entity)
      throws IOException {

  }
}
