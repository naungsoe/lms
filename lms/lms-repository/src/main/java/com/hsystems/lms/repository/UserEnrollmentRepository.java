package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.UserEnrollment;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface UserEnrollmentRepository
    extends Repository<UserEnrollment> {

  List<UserEnrollment> findAllBy(
      String schoolId, String lastId, int limit)
      throws IOException;

  List<UserEnrollment> findAllBy(String schoolId, String hostId)
      throws IOException;
}
