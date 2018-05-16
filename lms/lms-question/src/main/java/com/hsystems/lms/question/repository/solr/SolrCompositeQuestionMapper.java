package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Collections;
import java.util.List;

public final class SolrCompositeQuestionMapper
    implements Mapper<SolrDocument, Question> {

  private static final String BODY_FIELD = "body";
  private static final String HINT_FIELD = "hint";
  private static final String EXPLANATION_FIELD = "explanation";

  private final SolrDocument document;

  public SolrCompositeQuestionMapper(SolrDocument document) {
    this.document = document;
  }

  @Override
  public Question from(SolrDocument source) {
    String body = SolrUtils.getString(document, BODY_FIELD);
    String hint = SolrUtils.getString(document, HINT_FIELD);
    String explanation = SolrUtils.getString(document, EXPLANATION_FIELD);
    List<QuestionComponent> components = Collections.emptyList();
    return new CompositeQuestion(body, hint, explanation, components);
  }
}