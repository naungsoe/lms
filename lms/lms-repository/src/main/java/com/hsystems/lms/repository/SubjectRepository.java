package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Subject;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SubjectRepository
    extends Repository<Subject> {

  List<Subject> findAllBy(String schoolId)
      throws IOException;
}
