package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public abstract class SolrQuestionMapper<T extends Question>
    implements Mapper<SolrDocument, T> {

  private static final String BODY_FIELD = "body";

  protected String getBody(SolrDocument document) {
    return SolrUtils.getString(document, BODY_FIELD);
  }
}