package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.group.repository.solr.SolrGroupRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class GroupIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseGroupRepository hbaseGroupRepository;

  private final SolrGroupRepository solrGroupRepository;

  @Inject
  GroupIndexService(
      HBaseGroupRepository hbaseGroupRepository,
      SolrGroupRepository solrGroupRepository) {

    this.hbaseGroupRepository = hbaseGroupRepository;
    this.solrGroupRepository = solrGroupRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<Group>> groups
          = hbaseGroupRepository.findAllBy(lastId, INDEX_LIMIT);
      solrGroupRepository.addAll(groups);

      numFound = groups.size();

      Auditable<Group> group = groups.get(numFound - 1);
      lastId = group.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<Group>> groupOptional
        = hbaseGroupRepository.findBy(id);

    if (groupOptional.isPresent()) {
      Auditable<Group> group = groupOptional.get();
      solrGroupRepository.add(group);
    }
  }
}