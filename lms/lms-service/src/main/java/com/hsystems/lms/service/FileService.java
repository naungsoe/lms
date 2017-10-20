package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.FileRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.service.model.file.FileResourceModel;
import com.hsystems.lms.service.model.LevelModel;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class FileService extends AbstractService {

  private final FileRepository fileRepository;

  private final IndexRepository indexRepository;

  @Inject
  FileService(
      FileRepository fileRepository,
      IndexRepository indexRepository) {

    this.fileRepository = fileRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<FileResourceModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    return null;
  }

  @Log
  public Optional<FileResourceModel> findBy(String id, Principal principal)
      throws IOException {

    return Optional.empty();
  }

  @Log
  public void create(
      LevelModel levelModel, Principal principal)
      throws IOException {


  }
}
