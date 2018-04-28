package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.GroupModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class GroupService extends AbstractService {

  private final Provider<Properties> propertiesProvider;

  private final IndexRepository indexRepository;

  @Inject
  GroupService(
      Provider<Properties> propertiesProvider,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<GroupModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<Group> queryResult
        = indexRepository.findAllBy(query, Group.class);
    List<Group> groups = queryResult.getItems();

    if (CollectionUtils.isEmpty(groups)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<GroupModel> resourceModels = getGroupModels(groups, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        resourceModels
    );
  }

  private List<GroupModel> getGroupModels(
      List<Group> groups, Configuration configuration) {

    List<GroupModel> groupModels = new ArrayList<>();

    for (Group group : groups) {
      GroupModel groupModel = getModel(group, GroupModel.class, configuration);
      groupModels.add(groupModel);
    }

    return groupModels;
  }

  @Log
  public Optional<GroupModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Group> groupOptional = indexRepository.findBy(id, Group.class);

    if (groupOptional.isPresent()) {
      Group group = groupOptional.get();
      Configuration configuration = Configuration.create(principal);
      GroupModel groupModel = getModel(group, GroupModel.class, configuration);
      return Optional.of(groupModel);
    }

    return Optional.empty();
  }
}