package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleResponse;

import org.apache.solr.common.SolrInputDocument;

import java.util.Collections;
import java.util.Enumeration;

public final class SolrMultipleResponseDocMapper
    extends SolrQuestionDocMapper<MultipleResponse> {

  private static final String BODY_FIELD = "body";

  private final SolrInputDocument document;

  public SolrMultipleResponseDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(MultipleResponse source) {
    document.addField(BODY_FIELD, source.getBody());

    SolrChoiceOptionDocsMapper optionDocsMapper
        = new SolrChoiceOptionDocsMapper(document);
    Enumeration<ChoiceOption> enumeration = source.getOptions();
    return optionDocsMapper.from(Collections.list(enumeration));
  }
}