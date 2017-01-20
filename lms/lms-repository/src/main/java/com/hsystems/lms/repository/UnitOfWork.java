package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface UnitOfWork {

  void registerNew(Entity entity)
      throws IOException;

  void registerDirty(Entity entity)
      throws IOException;

  void registerDeleted(Entity entity)
      throws IOException;

  void commit()
      throws IOException;

  void rollback()
      throws IOException;

  long getTimestamp(String id)
      throws IOException;
}
