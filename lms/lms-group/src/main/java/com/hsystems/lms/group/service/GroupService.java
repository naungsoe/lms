package com.hsystems.lms.group.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.group.repository.solr.SolrGroupRepository;
import com.hsystems.lms.group.service.mapper.GroupModelMapper;
import com.hsystems.lms.group.service.model.GroupModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class GroupService {

  private final HBaseGroupRepository hbaseGroupRepository;

  private final SolrGroupRepository solrGroupRepository;

  private final GroupModelMapper groupMapper;

  @Inject
  GroupService(
      HBaseGroupRepository hbaseGroupRepository,
      SolrGroupRepository solrGroupRepository) {

    this.hbaseGroupRepository = hbaseGroupRepository;
    this.solrGroupRepository = solrGroupRepository;
    this.groupMapper = new GroupModelMapper();
  }

  @Log
  @Requires(GroupPermission.VIEW_GROUP)
  public QueryResult<GroupModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<Group>> queryResult
        = solrGroupRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<Group>> groups = queryResult.getItems();

    if (CollectionUtils.isEmpty(groups)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<GroupModel> groupModels = new ArrayList<>();

    for (Auditable<Group> group : groups) {
      GroupModel groupModel = groupMapper.from(group);
      groupModels.add(groupModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, groupModels);
  }

  @Log
  @Requires(GroupPermission.VIEW_GROUP)
  public Optional<GroupModel> findBy(String id)
      throws IOException {

    Optional<Auditable<Group>> groupOptional
        = hbaseGroupRepository.findBy(id);

    if (groupOptional.isPresent()) {
      Auditable<Group> group = groupOptional.get();
      GroupModel groupModel = groupMapper.from(group);
      return Optional.of(groupModel);
    }

    return Optional.empty();
  }
}