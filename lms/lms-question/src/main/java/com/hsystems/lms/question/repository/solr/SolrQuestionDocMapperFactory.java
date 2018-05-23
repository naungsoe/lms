package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.repository.entity.MultipleResponse;
import com.hsystems.lms.question.repository.entity.Question;

import org.apache.solr.common.SolrInputDocument;

public final class SolrQuestionDocMapperFactory {

  public SolrQuestionDocMapperFactory() {

  }

  public SolrQuestionDocMapper create(
      Question question, SolrInputDocument document) {

    if (question instanceof MultipleChoice) {
      return new SolrMultipleChoiceDocMapper(document);

    } else if (question instanceof MultipleResponse) {
      return new SolrMultipleResponseDocMapper(document);

    } else {
      throw new IllegalArgumentException(
          "not supported question type");
    }
  }
}