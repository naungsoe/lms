package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRepository;
import com.hsystems.lms.subject.repository.solr.SolrSubjectRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class SubjectIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseSubjectRepository hbaseSubjectRepository;

  private final SolrSubjectRepository solrSubjectRepository;

  @Inject
  SubjectIndexService(
      HBaseSubjectRepository hbaseSubjectRepository,
      SolrSubjectRepository solrSubjectRepository) {

    this.hbaseSubjectRepository = hbaseSubjectRepository;
    this.solrSubjectRepository = solrSubjectRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<Subject>> subjects
          = hbaseSubjectRepository.findAllBy(lastId, INDEX_LIMIT);
      solrSubjectRepository.addAll(subjects);

      numFound = subjects.size();

      Auditable<Subject> subject = subjects.get(numFound - 1);
      lastId = subject.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<Subject>> subjectOptional
        = hbaseSubjectRepository.findBy(id);

    if (subjectOptional.isPresent()) {
      Auditable<Subject> subject = subjectOptional.get();
      solrSubjectRepository.add(subject);
    }
  }
}