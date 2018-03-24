package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.hbase.mapper.HBaseSubjectMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public class HBaseSubjectRepository extends HBaseAbstractRepository
    implements SubjectRepository {

  private final HBaseClient client;

  private final HBaseSubjectMapper subjectMapper;

  @Inject
  HBaseSubjectRepository(
      HBaseClient client,
      HBaseSubjectMapper subjectMapper) {

    this.client = client;
    this.subjectMapper = subjectMapper;
  }

  @Override
  public Optional<Subject> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Subject.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return subjectMapper.getEntity(results);
  }

  @Override
  public List<Subject> findAllBy(String schoolId)
    throws IOException {

    String startRowKey = getExclusiveStartRowKey(schoolId);
    Scan scan = getRowKeyFilterScan(schoolId);
    scan.setStartRow(Bytes.toBytes(startRowKey));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(Subject.class);
    List<Result> results = client.scan(scan, tableName);
    return subjectMapper.getEntities(results);
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
