package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class ComponentService extends AbstractService {

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  ComponentService(
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<ComponentModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    QueryResult<Component> queryResult
        = indexRepository.findAllBy(query, Component.class);
    List<Component> components = queryResult.getItems();

    if (CollectionUtils.isEmpty(components)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        getComponentModels(components, configuration)
    );
  }

  private List<ComponentModel> getComponentModels(
      List<Component> components, Configuration configuration) {

    List<ComponentModel> componentModels = new ArrayList<>();

    for (Component component : components) {
      ComponentModel componentModel = getModel(component,
          ComponentModel.class, configuration);
      componentModels.add(componentModel);
    }

    return componentModels;
  }

  @Log
  public Optional<ComponentModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Component> componentOptional
        = indexRepository.findBy(id, Component.class);

    if (componentOptional.isPresent()) {
      Component component = componentOptional.get();
      Configuration configuration = Configuration.create(principal);
      ComponentModel componentModel = getModel(component,
          ComponentModel.class, configuration);
      return Optional.of(componentModel);
    }

    return Optional.empty();
  }
}
