package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;

/**
 * Created by naungsoe on 15/10/16.
 */
public class IndexService extends BaseService {

  private final IndexRepository indexRepository;

  @Inject
  IndexService(IndexRepository indexRepository) {
    this.indexRepository = indexRepository;
  }

  @Log
  public void index(Entity entity)
      throws IOException {

    indexRepository.save(entity);
  }
}