package com.hsystems.lms.lesson.repository.solr;

import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrComponentMapperFactory;
import com.hsystems.lms.question.repository.solr.SolrQuestionComponentMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrLessonComponentMapperFactory
    implements SolrComponentMapperFactory {

  private static final String COMPONENT_TYPE_FIELD = "component_type";

  public SolrLessonComponentMapperFactory() {

  }

  @Override
  public SolrComponentMapper create(SolrDocument document) {
    switch (SolrUtils.getString(document, COMPONENT_TYPE_FIELD)) {
      case "ActivityComponent":
        return new SolrActivityComponentMapper();
      case "ContentComponent":
        return new SolrContentComponentMapper();
      case "QuestionComponent":
        return new SolrQuestionComponentMapper();
      default:
        throw new IllegalArgumentException(
            "not supported component type");
    }
  }
}