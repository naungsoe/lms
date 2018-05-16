package com.hsystems.lms.subject.repository;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRepository;
import com.hsystems.lms.subject.repository.solr.SolrSubjectRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlSubjectRepository implements SubjectRepository {

  private final SolrSubjectRepository solrSubjectRepository;

  private final HBaseSubjectRepository hbaseSubjectRepository;

  @Inject
  public NoSqlSubjectRepository(
      SolrSubjectRepository solrSubjectRepository,
      HBaseSubjectRepository hbaseSubjectRepository) {

    this.solrSubjectRepository = solrSubjectRepository;
    this.hbaseSubjectRepository = hbaseSubjectRepository;
  }

  @Override
  public QueryResult<Auditable<Subject>> findAllBy(Query query)
      throws IOException {

    return solrSubjectRepository.findAllBy(query);
  }

  @Override
  public Optional<Auditable<Subject>> findBy(String id)
      throws IOException {

    return solrSubjectRepository.findBy(id);
  }

  @Override
  public void add(Auditable<Subject> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<Subject> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<Subject> entity)
      throws IOException {

  }
}