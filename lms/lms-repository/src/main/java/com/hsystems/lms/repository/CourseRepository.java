package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.course.CourseResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface CourseRepository
    extends Repository<CourseResource> {

  List<CourseResource> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}
