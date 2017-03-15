package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.User;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class IndexService extends BaseService {

  private final IndexRepository indexRepository;

  private final UserRepository userRepository;

  private final QuestionRepository questionRepository;

  @Inject
  IndexService(
      IndexRepository indexRepository,
      UserRepository userRepository,
      QuestionRepository questionRepository) {

    this.indexRepository = indexRepository;
    this.userRepository = userRepository;
    this.questionRepository = questionRepository;
  }

  @Log
  public void index(String type, String id)
      throws IOException {

    switch (Enum.valueOf(EntityType.class, type.toUpperCase())) {
      case USER:
        Optional<User> userOptional = userRepository.findBy(id);

        if (userOptional.isPresent()) {
          User user = userOptional.get();
          indexRepository.save(user);
        }
        break;
      default:
        break;
    }
  }
}