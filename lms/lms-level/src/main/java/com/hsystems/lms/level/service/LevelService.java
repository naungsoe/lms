package com.hsystems.lms.level.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRepository;
import com.hsystems.lms.level.repository.solr.SolrLevelRepository;
import com.hsystems.lms.level.service.mapper.LevelModelMapper;
import com.hsystems.lms.level.service.model.LevelModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class LevelService {

  private final HBaseLevelRepository hbaseLevelRepository;

  private final SolrLevelRepository solrLevelRepository;

  private final LevelModelMapper levelMapper;

  @Inject
  LevelService(
      HBaseLevelRepository hbaseLevelRepository,
      SolrLevelRepository solrLevelRepository) {

    this.hbaseLevelRepository = hbaseLevelRepository;
    this.solrLevelRepository = solrLevelRepository;
    this.levelMapper = new LevelModelMapper();
  }

  @Log
  @Requires(LevelPermission.VIEW_LEVEL)
  public QueryResult<LevelModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<Level>> queryResult
        = solrLevelRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<Level>> levels = queryResult.getItems();

    if (CollectionUtils.isEmpty(levels)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<LevelModel> levelModels = new ArrayList<>();

    for (Auditable<Level> level : levels) {
      LevelModel levelModel = levelMapper.from(level);
      levelModels.add(levelModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, levelModels);
  }

  @Log
  @Requires(LevelPermission.VIEW_LEVEL)
  public Optional<LevelModel> findBy(String id)
      throws IOException {

    Optional<Auditable<Level>> levelOptional
        = hbaseLevelRepository.findBy(id);

    if (levelOptional.isPresent()) {
      Auditable<Level> level = levelOptional.get();
      LevelModel levelModel = levelMapper.from(level);
      return Optional.of(levelModel);
    }

    return Optional.empty();
  }
}