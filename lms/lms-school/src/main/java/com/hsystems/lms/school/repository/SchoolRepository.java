package com.hsystems.lms.school.repository;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.repository.Repository;
import com.hsystems.lms.school.repository.entity.School;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SchoolRepository
    extends Repository<Auditable<School>> {

  List<Auditable<School>> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException;
}