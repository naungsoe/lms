package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.solr.common.SolrInputDocument;

public final class SolrQuestionComponentDocMapper
    implements Mapper<QuestionComponent, SolrInputDocument> {

  private static final String PARENT_ID_FIELD = "parentId";
  private static final String RESOURCE_ID_FIELD = "resourceId";

  private final String parentId;

  private final String resourceId;

  private final SolrQuestionDocMapperFactory mapperFactory;

  public SolrQuestionComponentDocMapper(String parentId, String resourceId) {
    this.parentId = parentId;
    this.resourceId = resourceId;
    this.mapperFactory = new SolrQuestionDocMapperFactory();
  }

  @Override
  public SolrInputDocument from(QuestionComponent source) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField(PARENT_ID_FIELD, parentId);
    document.addField(RESOURCE_ID_FIELD, resourceId);
    return document;
  }
}