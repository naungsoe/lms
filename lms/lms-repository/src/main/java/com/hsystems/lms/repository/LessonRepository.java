package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.lesson.Lesson;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface LessonRepository
    extends Repository<Lesson> {

  List<Lesson> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}
