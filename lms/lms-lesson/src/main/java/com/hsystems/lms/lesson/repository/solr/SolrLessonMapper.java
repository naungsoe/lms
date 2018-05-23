package com.hsystems.lms.lesson.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.lesson.repository.entity.Lesson;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrLessonMapper implements Mapper<SolrDocument, Lesson> {

  private static final String TITLE_FIELD = "title";
  private static final String DESCRIPTION_FIELD = "description";

  private final List<Component> components;

  public SolrLessonMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Lesson from(SolrDocument source) {
    String title = SolrUtils.getString(source, TITLE_FIELD);
    String description = SolrUtils.getString(source, DESCRIPTION_FIELD);
    return new Lesson(title, description, components);
  }
}