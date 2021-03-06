package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.service.model.LevelModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 15/10/16.
 */
public class LevelService extends AbstractService {

  private final LevelRepository levelRepository;

  private final IndexRepository indexRepository;

  @Inject
  LevelService(
      LevelRepository levelRepository,
      IndexRepository indexRepository) {

    this.levelRepository = levelRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public List<LevelModel> findAllBy(Principal principal)
      throws IOException {

    Query query = new Query();
    addSchoolFilter(query, principal);

    QueryResult<Level> queryResult
        = indexRepository.findAllBy(query, Level.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Collections.emptyList();
    }

    List<Level> levels = queryResult.getItems();
    return getLevelModels(levels);
  }

  private List<LevelModel> getLevelModels(List<Level> levels) {
    List<LevelModel> levelModels = new ArrayList<>();

    for (Level level : levels) {
      LevelModel levelModel = getModel(level, LevelModel.class);
      levelModels.add(levelModel);
    }

    return levelModels;
  }

  @Log
  public void create(
      LevelModel levelModel, Principal principal)
      throws IOException {

    UserModel userModel = (UserModel) principal;
    levelModel.setSchool(userModel.getSchool());

    Level level = getEntity(levelModel, Level.class);
    levelRepository.create(level);
    indexRepository.save(level);
  }
}
