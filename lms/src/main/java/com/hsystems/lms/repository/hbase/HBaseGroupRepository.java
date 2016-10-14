package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.model.Group;
import com.hsystems.lms.provider.hbase.HBaseClient;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by administrator on 14/10/16.
 */
public class HBaseGroupRepository implements GroupRepository {

  private Provider<HBaseClient> hBaseClientProvider;

  @Inject
  HBaseGroupRepository(Provider<HBaseClient> hBaseClientProvider) {
    this.hBaseClientProvider = hBaseClientProvider;
  }

  public Optional<Group> findBy(String key)
      throws RepositoryException {

    return Optional.empty();
  }
}
