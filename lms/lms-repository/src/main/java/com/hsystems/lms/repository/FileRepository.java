package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.file.FileResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface FileRepository
    extends Repository<FileResource> {

  List<FileResource> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;

  List<FileResource> findAllBy(String schoolId, String parentId)
      throws IOException;
}