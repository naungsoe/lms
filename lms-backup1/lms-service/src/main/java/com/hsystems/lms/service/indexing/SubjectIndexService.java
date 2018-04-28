package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class SubjectIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final SubjectRepository subjectRepository;

  @Inject
  SubjectIndexService(
      IndexRepository indexRepository,
      SubjectRepository subjectRepository) {

    this.indexRepository = indexRepository;
    this.subjectRepository = subjectRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    List<Subject> subjects = subjectRepository.findAllBy(schoolId);
    indexRepository.save(subjects);
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexBy(String id)
      throws IOException {

    Optional<Subject> subjectOptional = subjectRepository.findBy(id);

    if (subjectOptional.isPresent()) {
      Subject subject = subjectOptional.get();
      indexRepository.save(subject);
    }
  }
}