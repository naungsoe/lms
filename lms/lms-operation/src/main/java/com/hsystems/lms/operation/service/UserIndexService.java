package com.hsystems.lms.operation.service;

import com.google.inject.Inject;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.user.repository.solr.SolrUserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class UserIndexService {

  private static final int INDEX_LIMIT = 50;

  private final HBaseUserRepository hbaseUserRepository;

  private final SolrUserRepository solrUserRepository;

  @Inject
  UserIndexService(
      HBaseUserRepository hbaseUserRepository,
      SolrUserRepository solrUserRepository) {

    this.hbaseUserRepository = hbaseUserRepository;
    this.solrUserRepository = solrUserRepository;
  }

  public void indexAll()
      throws IOException {

    String lastId = "";
    int numFound;

    do {
      List<Auditable<AppUser>> users
          = hbaseUserRepository.findAllBy(lastId, INDEX_LIMIT);
      solrUserRepository.addAll(users);

      numFound = users.size();

      Auditable<AppUser> user = users.get(numFound - 1);
      lastId = user.getEntity().getId();

    } while (!isLastPage(numFound));
  }

  private boolean isLastPage(int numFound) {
    return (numFound == 0) || (numFound < INDEX_LIMIT);
  }

  public void index(String id)
      throws IOException {

    Optional<Auditable<AppUser>> userOptional
        = hbaseUserRepository.findBy(id);

    if (userOptional.isPresent()) {
      Auditable<AppUser> user = userOptional.get();
      solrUserRepository.add(user);
    }
  }
}