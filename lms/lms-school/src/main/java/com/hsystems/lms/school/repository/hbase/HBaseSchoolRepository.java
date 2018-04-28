package com.hsystems.lms.school.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.repository.query.Query;
import com.hsystems.lms.entity.repository.query.QueryResult;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanner;
import com.hsystems.lms.school.repository.SchoolRepository;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.solr.SolrClient;

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
public final class HBaseSchoolRepository implements SchoolRepository {

  private static final TableName SCHOOL_TABLE
      = TableName.valueOf("lms:schools");

  private final HBaseClient hbaseClient;

  private final SolrClient solrClient;

  @Inject
  HBaseSchoolRepository(
      HBaseClient hbaseClient,
      SolrClient solrClient) {

    this.hbaseClient = hbaseClient;
    this.solrClient = solrClient;
  }

  @Override
  public QueryResult<Auditable<School>> findAllBy(Query query)
      throws IOException {

    return null;
  }

  @Override
  public List<Auditable<School>> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException {


    return null;
  }

  @Override
  public Optional<Auditable<School>> findBy(String id)
      throws IOException {

    Scan scan = HBaseScanner.createRowKeyFilter(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(HBaseScanner.MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, SCHOOL_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    return schoolMapper.getEntity(results);
  }

  @Override
  public void add(List<Auditable<School>> entity) throws IOException {

  }

  @Override
  public void add(Auditable<School> entity) throws IOException {

  }

  @Override
  public void update(Auditable<School> entity) throws IOException {

  }

  @Override
  public void remove(Auditable<School> entity) throws IOException {

  }
}
