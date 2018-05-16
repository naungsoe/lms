package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrQuestionComponentMapper
    implements SolrComponentMapper {

  private static final String ID_FIELD = "id";
  private static final String SCORE_FIELD = "score";
  private static final String ORDER_FIELD = "order";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";

  private static final String QUESTION_FIELD_FORMAT = "%s.question";

  private final String resourceId;

  public SolrQuestionComponentMapper(String resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public Nested<Component> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String fieldName = SolrUtils.getString(source, FIELD_NAME_FIELD);
    String questionFieldName = String.format(QUESTION_FIELD_FORMAT, fieldName);
    SolrQuestionMapper questionMapper
        = new SolrQuestionMapper(id, questionFieldName);
    Question question = questionMapper.from(source);
    long score = SolrUtils.getLong(source, SCORE_FIELD);
    int order = SolrUtils.getInteger(source, ORDER_FIELD);
    String parentId = SolrUtils.getString(source, PARENT_ID_FIELD);
    QuestionComponent questionComponent
        = new QuestionComponent(id, question, score, order);
    return new Nested<>(questionComponent, resourceId, parentId);
  }
}