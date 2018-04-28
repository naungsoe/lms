package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.ComponentAttempt;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 8/2/18.
 */
public interface AttemptRepository
    extends Repository<ComponentAttempt> {

  List<ComponentAttempt> findAllBy(String parentId)
      throws IOException;

  void save(ComponentAttempt entity, String parentId)
      throws IOException;
}