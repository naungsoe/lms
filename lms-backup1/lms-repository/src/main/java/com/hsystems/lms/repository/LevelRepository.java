package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Level;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface LevelRepository
    extends Repository<Level> {

  List<Level> findAllBy(String schoolId)
      throws IOException;
}
