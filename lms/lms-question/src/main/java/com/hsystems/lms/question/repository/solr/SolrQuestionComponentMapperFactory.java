package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrComponentMapperFactory;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrQuestionComponentMapperFactory
    implements SolrComponentMapperFactory {

  private static final String COMPONENT_TYPE_FIELD = "component_type";

  public SolrQuestionComponentMapperFactory() {

  }

  @Override
  public SolrComponentMapper create(SolrDocument document) {
    switch (SolrUtils.getString(document, COMPONENT_TYPE_FIELD)) {
      case "QuestionComponent":
        return new SolrQuestionComponentMapper();
      default:
        throw new IllegalArgumentException(
            "not supported component type");
    }
  }
}