package com.hsystems.lms.component.repository.solr;

import com.google.inject.Inject;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.solr.SolrQueryMapper;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrComponentRepository
    implements Repository<Nested<Component>> {

  private static final String COMPONENT_COLLECTION = "lms.components";

  private static final String RESOURCE_ID_FIELD = "resourceId";

  private final SolrClient solrClient;

  private SolrQueryMapper queryMapper;

  private SolrComponentMapper componentMapper;

  private SolrComponentDocMapper componentDocMapper;

  @Inject
  SolrComponentRepository(SolrClient solrClient) {
    this.solrClient = solrClient;

    String typeName = Component.class.getSimpleName();
    this.queryMapper = new SolrQueryMapper(String.format("*%s", typeName));
  }

  public void setComponentMapper(SolrComponentMapper componentMapper) {
    this.componentMapper = componentMapper;
  }

  public void setComponentDocMapper(SolrComponentDocMapper componentDocMapper) {
    this.componentDocMapper = componentDocMapper;
  }

  public List<Nested<Component>> findAllBy(String resourceId)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual(RESOURCE_ID_FIELD, resourceId));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();
    List<Nested<Component>> components = new ArrayList<>();
    documentList.forEach(document -> {
      Nested<Component> component = componentMapper.from(document);
      components.add(component);
    });

    return components;
  }

  private QueryResponse executeQuery(Query query)
      throws IOException {

    SolrQuery solrQuery = queryMapper.from(query);
    return solrClient.query(solrQuery, COMPONENT_COLLECTION);
  }

  @Override
  public Optional<Nested<Component>> findBy(String id)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("id", id));

    QueryResponse queryResponse = executeQuery(query);
    SolrDocumentList documentList = queryResponse.getResults();

    if (CollectionUtils.isEmpty(documentList)) {
      return Optional.empty();
    }

    SolrDocument document = documentList.get(0);
    Nested<Component> component = componentMapper.from(document);
    return Optional.of(component);
  }

  @Override
  public void add(Nested<Component> entity)
      throws IOException {

    SolrInputDocument document = componentDocMapper.from(entity);
    solrClient.save(document, COMPONENT_COLLECTION);
  }

  @Override
  public void update(Nested<Component> entity)
      throws IOException {

    SolrInputDocument document = componentDocMapper.from(entity);
    solrClient.save(document, COMPONENT_COLLECTION);
  }

  @Override
  public void remove(Nested<Component> entity)
      throws IOException {

    String id = entity.getId();
    solrClient.deleteBy(id, COMPONENT_COLLECTION);
  }
}