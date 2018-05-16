package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.question.repository.entity.ChoiceOption;

import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.List;

public final class SolrChoiceOptionDocsMapper
    implements Mapper<List<ChoiceOption>, List<SolrInputDocument>> {

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String BODY_FIELD = "body";
  private static final String FEEDBACK_FIELD = "feedback";
  private static final String CORRECT_FIELD = "correct";
  private static final String ORDER_FIELD = "order";

  private static final String OPTIONS_FIELD_FORMAT = "%s.options";

  private final String parentId;

  private final String fieldName;

  public SolrChoiceOptionDocsMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public List<SolrInputDocument> from(List<ChoiceOption> source) {
    List<SolrInputDocument> documents = new ArrayList<>();
    String optionFieldName = String.format(OPTIONS_FIELD_FORMAT, fieldName);

    for (ChoiceOption option : source) {
      SolrInputDocument document = new SolrInputDocument();
      document.addField(ID_FIELD, CommonUtils.genUniqueKey());
      document.addField(ENTITY_ID_FIELD, option.getId());
      document.addField(PARENT_ID_FIELD, parentId);
      document.addField(FIELD_NAME_FIELD, optionFieldName);
      document.addField(BODY_FIELD, option.getBody());
      document.addField(FEEDBACK_FIELD, option.getFeedback());
      document.addField(CORRECT_FIELD, option.isCorrect());
      document.addField(ORDER_FIELD, option.getOrder());
      documents.add(document);
    }

    return documents;
  }
}