package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.repository.entity.MultipleResponse;
import com.hsystems.lms.question.repository.entity.Question;

import org.apache.solr.common.SolrInputDocument;

public final class SolrQuestionDocMapper
    implements Mapper<Question, SolrInputDocument> {

  private final String parentId;

  private final String fieldName;

  public SolrQuestionDocMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public SolrInputDocument from(Question source) {
    if (source instanceof MultipleChoice) {
      SolrMultipleChoiceDocMapper multipleChoiceDocMapper
          = new SolrMultipleChoiceDocMapper(parentId, fieldName);
      return multipleChoiceDocMapper.from((MultipleChoice) source);

    } else if (source instanceof MultipleResponse) {
      SolrMultipleResponseDocMapper multipleResponseDocMapper
          = new SolrMultipleResponseDocMapper(parentId, fieldName);
      return multipleResponseDocMapper.from((MultipleResponse) source);

    } else if (source instanceof CompositeQuestion) {
      SolrCompositeQuestionDocMapper compositeQuestionDocMapper
          = new SolrCompositeQuestionDocMapper(parentId, fieldName);
      return compositeQuestionDocMapper.from((CompositeQuestion) source);
    }

    return new SolrInputDocument();
  }
}