package com.hsystems.lms.quiz.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.quiz.repository.entity.Quiz;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrQuizMapper implements Mapper<SolrDocument, Quiz> {

  private static final String TITLE_FIELD = "title";
  private static final String DESCRIPTION_FIELD = "description";

  private final List<Component> components;

  public SolrQuizMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Quiz from(SolrDocument source) {
    String title = SolrUtils.getString(source, TITLE_FIELD);
    String description = SolrUtils.getString(source, DESCRIPTION_FIELD);
    return new Quiz(title, description, components);
  }
}