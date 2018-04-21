package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LevelRepository;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class LevelIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final LevelRepository levelRepository;

  @Inject
  LevelIndexService(
      IndexRepository indexRepository,
      LevelRepository levelRepository) {

    this.indexRepository = indexRepository;
    this.levelRepository = levelRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    List<Level> levels = levelRepository.findAllBy(schoolId);
    indexRepository.save(levels);
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexBy(String id)
      throws IOException {

    Optional<Level> levelOptional = levelRepository.findBy(id);

    if (levelOptional.isPresent()) {
      Level level = levelOptional.get();
      indexRepository.save(level);
    }
  }
}