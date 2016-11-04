package com.hsystems.lms.repository;

import com.hsystems.lms.model.ShareLog;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface ShareLogRepository {

  List<ShareLog> findAllBy(String id)
      throws RepositoryException;
}
