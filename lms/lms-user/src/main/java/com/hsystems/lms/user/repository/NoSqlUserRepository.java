package com.hsystems.lms.user.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.user.repository.solr.SolrUserRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlUserRepository implements UserRepository {

  private final SolrUserRepository solrUserRepository;

  private final HBaseUserRepository hbaseUserRepository;

  @Inject
  public NoSqlUserRepository(
      SolrUserRepository solrUserRepository,
      HBaseUserRepository hbaseUserRepository) {

    this.solrUserRepository = solrUserRepository;
    this.hbaseUserRepository = hbaseUserRepository;
  }

  @Override
  public QueryResult<Auditable<AppUser>> findAllBy(Query query)
      throws IOException {

    return solrUserRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<AppUser>> findBy(String id)
      throws IOException {

    return solrUserRepository.findBy(id);
  }

  @Override
  public void add(Auditable<AppUser> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<AppUser> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<AppUser> entity)
      throws IOException {

  }
}