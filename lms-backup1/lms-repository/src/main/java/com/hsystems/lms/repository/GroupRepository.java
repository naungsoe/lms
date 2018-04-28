package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Group;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface GroupRepository
    extends Repository<Group> {

  List<Group> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}