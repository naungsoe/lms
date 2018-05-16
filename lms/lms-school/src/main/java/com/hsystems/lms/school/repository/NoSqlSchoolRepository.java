package com.hsystems.lms.school.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.school.repository.solr.SolrSchoolRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlSchoolRepository implements SchoolRepository {

  private final SolrSchoolRepository solrSchoolRepository;

  private final HBaseSchoolRepository hbaseSchoolRepository;

  @Inject
  public NoSqlSchoolRepository(
      SolrSchoolRepository solrSchoolRepository,
      HBaseSchoolRepository hbaseSchoolRepository) {

    this.solrSchoolRepository = solrSchoolRepository;
    this.hbaseSchoolRepository = hbaseSchoolRepository;
  }

  @Override
  public QueryResult<Auditable<School>> findAllBy(Query query)
      throws IOException {

    return solrSchoolRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<School>> findBy(String id)
      throws IOException {

    return solrSchoolRepository.findBy(id);
  }

  @Override
  public void add(Auditable<School> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<School> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<School> entity)
      throws IOException {

  }
}