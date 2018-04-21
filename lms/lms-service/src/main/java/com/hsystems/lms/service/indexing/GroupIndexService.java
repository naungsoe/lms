package com.hsystems.lms.service.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.service.AppPermission;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class GroupIndexService extends IndexAbstractService {

  private final IndexRepository indexRepository;

  private final GroupRepository groupRepository;

  @Inject
  GroupIndexService(
      IndexRepository indexRepository,
      GroupRepository groupRepository) {

    this.indexRepository = indexRepository;
    this.groupRepository = groupRepository;
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexAllBy(String schoolId)
      throws IOException {

    String lastId = schoolId;
    int numFound;

    do {
      List<Group> groups = groupRepository.findAllBy(
          schoolId, lastId, INDEX_LIMIT);
      indexRepository.save(groups);

      numFound = groups.size();
      lastId = groups.get(numFound - 1).getId();

    } while (!isLastPage(numFound));
  }

  @Log
  @Requires(AppPermission.ADMINISTRATION)
  public void indexBy(String id)
      throws IOException {

    Optional<Group> groupOptional = groupRepository.findBy(id);

    if (groupOptional.isPresent()) {
      Group group = groupOptional.get();
      indexRepository.save(group);
    }
  }
}