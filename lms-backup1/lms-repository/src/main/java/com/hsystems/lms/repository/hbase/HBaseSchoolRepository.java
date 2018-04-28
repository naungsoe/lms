package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.hbase.mapper.HBaseSchoolMapper;
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
public class HBaseSchoolRepository extends HBaseAbstractRepository
    implements SchoolRepository {

  private final HBaseClient client;

  private final HBaseSchoolMapper schoolMapper;

  @Inject
  HBaseSchoolRepository(
      HBaseClient client,
      HBaseSchoolMapper schoolMapper) {

    this.client = client;
    this.schoolMapper = schoolMapper;
  }

  @Override
  public Optional<School> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    TableName tableName = getTableName(School.class);
    List<Result> results = client.scan(scan, tableName);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return schoolMapper.getEntity(results);
  }

  @Override
  public void create(School entity)
      throws IOException {

  }

  @Override
  public void update(School entity)
      throws IOException {

  }

  @Override
  public void delete(School entity)
      throws IOException {

  }
}
