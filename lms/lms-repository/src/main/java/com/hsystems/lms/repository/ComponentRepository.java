package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface ComponentRepository
    extends Repository<Component> {

  List<Component> findAllBy(String schoolId, String resourceId)
      throws IOException;
}