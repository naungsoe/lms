package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.special.UnknownQuestion;
import com.hsystems.lms.question.service.model.QuestionType;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class SolrQuestionMapper
    implements Mapper<SolrDocument, Question> {

  private static final String TYPE_FIELD = "type";

  private final String parentId;

  private final String fieldName;

  public SolrQuestionMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public Question from(SolrDocument source) {
    List<SolrDocument> childDocuments = source.getChildDocuments();
    Predicate<SolrDocument> questionDocument
        = SolrUtils.isChildDocument(parentId, fieldName);
    Optional<SolrDocument> documentOptional = childDocuments.stream()
        .filter(questionDocument).findFirst();

    if (documentOptional.isPresent()) {
      SolrDocument document = documentOptional.get();
      String type = SolrUtils.getString(document, TYPE_FIELD);

      switch (QuestionType.valueOf(type)) {
        case MULTIPLE_CHOICE:
          SolrMultipleChoiceMapper multipleChoiceMapper
              = new SolrMultipleChoiceMapper(document);
          return multipleChoiceMapper.from(source);
        case MULTIPLE_RESPONSE:
          SolrMultipleResponseMapper multipleResponseMapper
              = new SolrMultipleResponseMapper(document);
          return multipleResponseMapper.from(source);
        case COMPOSITE:
          SolrCompositeQuestionMapper compositeQuestionMapper
              = new SolrCompositeQuestionMapper(document);
          return compositeQuestionMapper.from(source);
        default:
          return new UnknownQuestion();
      }
    }

    return new UnknownQuestion();
  }
}