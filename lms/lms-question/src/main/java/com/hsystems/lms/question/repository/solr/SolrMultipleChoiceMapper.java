package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrMultipleChoiceMapper
    implements Mapper<SolrDocument, Question> {

  private static final String ID_FIELD = "id";
  private static final String BODY_FIELD = "body";
  private static final String HINT_FIELD = "hint";
  private static final String EXPLANATION_FIELD = "explanation";
  private static final String FIELD_NAME_FIELD = "fieldName";

  private final SolrDocument document;

  public SolrMultipleChoiceMapper(SolrDocument document) {
    this.document = document;
  }

  @Override
  public Question from(SolrDocument source) {
    String body = SolrUtils.getString(document, BODY_FIELD);
    String hint = SolrUtils.getString(document, HINT_FIELD);
    String explanation = SolrUtils.getString(document, EXPLANATION_FIELD);

    String parentId = SolrUtils.getString(document, ID_FIELD);
    String fieldName = SolrUtils.getString(document, FIELD_NAME_FIELD);
    SolrChoiceOptionsMapper optionsMapper
        = new SolrChoiceOptionsMapper(parentId, fieldName);
    List<ChoiceOption> options = optionsMapper.from(source);
    return new MultipleChoice(body, hint, explanation, options);
  }
}