package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.ShareLog;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface ShareLogRepository {

  Optional<ShareLog> findBy(String id)
      throws IOException;
}
