package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentDocMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.solr.common.SolrInputDocument;

public final class SolrQuestionComponentDocMapper
    implements SolrComponentDocMapper {

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String RESOURCE_ID_FIELD = "resourceId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String SCORE_FIELD = "score";
  private static final String ORDER_FIELD = "order";

  private final String resourceId;

  private final String parentId;

  private final String fieldName;

  public SolrQuestionComponentDocMapper(
      String resourceId,
      String parentId,
      String fieldName) {

    this.resourceId = resourceId;
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public SolrInputDocument from(Nested<Component> source) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField(ID_FIELD, CommonUtils.genUniqueKey());
    document.addField(ENTITY_ID_FIELD, source.getId());
    document.addField(PARENT_ID_FIELD, parentId);
    document.addField(RESOURCE_ID_FIELD, resourceId);
    document.addField(FIELD_NAME_FIELD, fieldName);

    QuestionComponent questionComponent
        = (QuestionComponent) source.getComponent();
    document.addField(SCORE_FIELD, questionComponent.getScore());
    document.addField(ORDER_FIELD, questionComponent.getOrder());

    Question question = questionComponent.getQuestion();
    SolrQuestionDocMapper questionDocMapper
        = new SolrQuestionDocMapper(source.getId(), fieldName);
    document.addChildDocument(questionDocMapper.from(question));
    return document;
  }
}