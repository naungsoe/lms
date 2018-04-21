package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class UserIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final UserRepository userRepository;

  @Inject
  UserIndexService(
      IndexRepository indexRepository,
      UserRepository userRepository) {

    this.indexRepository = indexRepository;
    this.userRepository = userRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<User> users = userRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(users);

      numFound = users.size();
      lastId = users.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexBy(String id)
      throws IOException {

    Optional<User> userOptional = userRepository.findBy(id);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      indexRepository.save(user);
    }
  }
}