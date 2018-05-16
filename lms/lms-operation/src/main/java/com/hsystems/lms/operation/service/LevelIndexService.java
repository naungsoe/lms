package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRepository;
import com.hsystems.lms.level.repository.solr.SolrLevelRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class LevelIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseLevelRepository hbaseLevelRepository;

  private final SolrLevelRepository solrLevelRepository;

  @Inject
  LevelIndexService(
      HBaseLevelRepository hbaseLevelRepository,
      SolrLevelRepository solrGroupRepository) {

    this.hbaseLevelRepository = hbaseLevelRepository;
    this.solrLevelRepository = solrGroupRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<Level>> levels
          = hbaseLevelRepository.findAllBy(lastId, INDEX_LIMIT);
      solrLevelRepository.addAll(levels);

      numFound = levels.size();

      Auditable<Level> level = levels.get(numFound - 1);
      lastId = level.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<Level>> levelOptional
        = hbaseLevelRepository.findBy(id);

    if (levelOptional.isPresent()) {
      Auditable<Level> level = levelOptional.get();
      solrLevelRepository.add(level);
    }
  }
}