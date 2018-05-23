package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Collections;
import java.util.List;

public final class SolrQuestionMapperFactory {

  private static final String TYPE_FIELD = "type";

  public SolrQuestionMapperFactory() {

  }

  public SolrQuestionMapper create(SolrDocument document) {
    return create(document, Collections.emptyList());
  }

  public SolrQuestionMapper create(
      SolrDocument document, List<Component> components) {

    switch (SolrUtils.getString(document, TYPE_FIELD)) {
      case "MultipleChoice":
        return new SolrMultipleChoiceMapper();
      case "MultipleResponse":
        return new SolrMultipleResponseMapper();
      case "CompositeQuestion":
        return new SolrCompositeQuestionMapper(components);
      default:
        throw new IllegalArgumentException(
            "not supported question type");
    }
  }
}