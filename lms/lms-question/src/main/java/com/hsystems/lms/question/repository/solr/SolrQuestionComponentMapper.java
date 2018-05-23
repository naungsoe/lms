package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrNestedComponentMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrQuestionComponentMapper
    implements SolrComponentMapper<QuestionComponent> {

  private static final String ID_FIELD = "id";
  private static final String SCORE_FIELD = "score";

  private final SolrQuestionMapperFactory mapperFactory;

  public SolrQuestionComponentMapper() {
    this.mapperFactory = new SolrQuestionMapperFactory();
  }

  @Override
  public Nested<QuestionComponent> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    long score = SolrUtils.getLong(source, SCORE_FIELD);
    SolrQuestionMapper<Question> questionMapper = mapperFactory.create(source);
    Question question = questionMapper.from(source);
    QuestionComponent component = new QuestionComponent(id, question, score);

    SolrNestedComponentMapper componentMapper
        = new SolrNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}