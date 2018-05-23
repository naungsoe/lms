package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.question.repository.entity.CompositeQuestion;

import org.apache.solr.common.SolrInputDocument;

public final class SolrCompositeQuestionDocMapper
    extends SolrQuestionDocMapper<CompositeQuestion> {

  private static final String BODY_FIELD = "body";

  private final SolrInputDocument document;

  public SolrCompositeQuestionDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(CompositeQuestion source) {
    document.addField(BODY_FIELD, source.getBody());
    return document;
  }
}