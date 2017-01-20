package com.hsystems.lms.repository;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by naungsoe on 6/1/17.
 */
public class RepositoryRegistry {

  private final Injector injector;

  @Inject
  RepositoryRegistry(Injector injector) {
    this.injector = injector;
  }

  public Repository getRepository(Class entityType) {
    try {
      String packageName = this.getClass().getPackage().getName();
      String repositoryName = String.format("%s.%s%s", packageName,
          entityType.getName(), Constants.REPOSITORY_SUFFIX);
      Class<?> repositoryType = Class.forName(repositoryName);
      return (Repository) injector.getInstance(repositoryType);

    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "error retrieving repository", e);
    }
  }

  public IndexRepository getIndexRepository() {
    return injector.getInstance(IndexRepository.class);
  }
}
