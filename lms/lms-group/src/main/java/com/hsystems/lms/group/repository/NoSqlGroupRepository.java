package com.hsystems.lms.group.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.group.repository.solr.SolrGroupRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlGroupRepository implements GroupRepository {

  private final SolrGroupRepository solrGroupRepository;

  private final HBaseGroupRepository hbaseGroupRepository;

  @Inject
  public NoSqlGroupRepository(
      SolrGroupRepository solrGroupRepository,
      HBaseGroupRepository hbaseGroupRepository) {

    this.solrGroupRepository = solrGroupRepository;
    this.hbaseGroupRepository = hbaseGroupRepository;
  }

  @Override
  public QueryResult<Auditable<Group>> findAllBy(Query query)
      throws IOException {

    return solrGroupRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<Group>> findBy(String id)
      throws IOException {

    return solrGroupRepository.findBy(id);
  }

  @Override
  public void add(Auditable<Group> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Group> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Group> entity)
      throws IOException {

  }
}