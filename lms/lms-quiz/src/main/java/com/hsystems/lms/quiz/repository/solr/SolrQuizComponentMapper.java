package com.hsystems.lms.quiz.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrNestedComponentMapper;
import com.hsystems.lms.quiz.repository.entity.Quiz;
import com.hsystems.lms.quiz.repository.entity.QuizComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Collections;
import java.util.List;

public final class SolrQuizComponentMapper
    implements SolrComponentMapper<QuizComponent> {

  private static final String ID_FIELD = "id";

  private final SolrQuizMapper quizMapper;

  public SolrQuizComponentMapper() {
    List<Component> components = Collections.emptyList();
    this.quizMapper = new SolrQuizMapper(components);
  }

  @Override
  public Nested<QuizComponent> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    Quiz quiz = quizMapper.from(source);
    QuizComponent component = new QuizComponent(id, quiz);

    SolrNestedComponentMapper componentMapper
        = new SolrNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}