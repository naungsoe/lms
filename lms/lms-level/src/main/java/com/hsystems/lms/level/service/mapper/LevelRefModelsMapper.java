package com.hsystems.lms.level.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.service.model.LevelModel;

import java.util.ArrayList;
import java.util.List;

public final class LevelRefModelsMapper
    implements Mapper<List<Level>, List<LevelModel>> {

  public LevelRefModelsMapper() {

  }

  @Override
  public List<LevelModel> from(List<Level> source) {
    List<LevelModel> levelModels = new ArrayList<>();

    for (Level level : source) {
      LevelModel levelModel = new LevelModel();
      levelModel.setId(level.getId());
      levelModel.setName(level.getName());
      levelModels.add(levelModel);
    }

    return levelModels;
  }
}