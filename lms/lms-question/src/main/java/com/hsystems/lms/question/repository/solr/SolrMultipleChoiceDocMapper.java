package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleChoice;

import org.apache.solr.common.SolrInputDocument;

import java.util.Collections;
import java.util.List;

public final class SolrMultipleChoiceDocMapper
    implements Mapper<MultipleChoice, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String BODY_FIELD = "body";
  private static final String HINT_FIELD = "hint";
  private static final String EXPLANATION_FIELD = "explanation";

  private final String parentId;

  private final String fieldName;

  public SolrMultipleChoiceDocMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public SolrInputDocument from(MultipleChoice source) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField(ID_FIELD, CommonUtils.genUniqueKey());
    document.addField(PARENT_ID_FIELD, parentId);
    document.addField(FIELD_NAME_FIELD, fieldName);
    document.addField(BODY_FIELD, source.getBody());
    document.addField(HINT_FIELD, source.getHint());
    document.addField(EXPLANATION_FIELD, source.getExplanation());

    SolrChoiceOptionDocsMapper optionDocsMapper
        = new SolrChoiceOptionDocsMapper(parentId, fieldName);
    List<ChoiceOption> options = Collections.list(source.getOptions());
    document.addChildDocuments(optionDocsMapper.from(options));
    return document;
  }
}