package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Mutation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface MutationRepository
    extends Repository<Mutation> {

  Optional<Mutation> findBy(String id, EntityType type)
      throws IOException;

  List<Mutation> findAllBy(
      String schoolId, String lastId, int limit, EntityType type)
      throws IOException;

  List<Mutation> findAllBy(
      String schoolId, Set<String> entityIds, EntityType type)
      throws IOException;
}