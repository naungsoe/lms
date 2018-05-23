package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;

import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public final class SolrChoiceOptionDocsMapper
    implements Mapper<List<ChoiceOption>, SolrInputDocument> {

  private static final String ID_FIELD_FORMAT = "options.%s.id";
  private static final String BODY_FIELD_FORMAT = "options.%s.body";
  private static final String CORRECT_FIELD_FORMAT = "options.%s.correct";

  private final SolrInputDocument document;

  public SolrChoiceOptionDocsMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(List<ChoiceOption> source) {
    for (int index = 0; index < source.size(); index++) {
      ChoiceOption option = source.get(0);
      String idField = String.format(ID_FIELD_FORMAT, index);
      document.addField(idField, option.getId());

      String nameField = String.format(BODY_FIELD_FORMAT, index);
      document.addField(nameField, option.getBody());

      String correctField = String.format(CORRECT_FIELD_FORMAT, index);
      document.addField(correctField, option.isCorrect());
    }

    return document;
  }
}