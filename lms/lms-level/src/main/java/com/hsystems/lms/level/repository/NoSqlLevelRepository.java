package com.hsystems.lms.level.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRepository;
import com.hsystems.lms.level.repository.solr.SolrLevelRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlLevelRepository implements LevelRepository {

  private final SolrLevelRepository solrLevelRepository;

  private final HBaseLevelRepository hbaseLevelRepository;

  @Inject
  public NoSqlLevelRepository(
      SolrLevelRepository solrLevelRepository,
      HBaseLevelRepository hbaseLevelRepository) {

    this.solrLevelRepository = solrLevelRepository;
    this.hbaseLevelRepository = hbaseLevelRepository;
  }

  @Override
  public QueryResult<Auditable<Level>> findAllBy(Query query)
      throws IOException {

    return solrLevelRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<Level>> findBy(String id)
      throws IOException {

    return solrLevelRepository.findBy(id);
  }

  @Override
  public void add(Auditable<Level> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Level> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Level> entity)
      throws IOException {

  }
}