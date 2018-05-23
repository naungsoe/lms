package com.hsystems.lms.lesson.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrNestedComponentMapper;
import com.hsystems.lms.lesson.repository.entity.Lesson;
import com.hsystems.lms.lesson.repository.entity.LessonComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Collections;
import java.util.List;

public final class SolrLessonComponentMapper
    implements SolrComponentMapper<LessonComponent> {

  private static final String ID_FIELD = "id";

  private final SolrLessonMapper lessonMapper;

  public SolrLessonComponentMapper() {
    List<Component> components = Collections.emptyList();
    this.lessonMapper = new SolrLessonMapper(components);
  }

  @Override
  public Nested<LessonComponent> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    Lesson lesson = lessonMapper.from(source);
    LessonComponent component = new LessonComponent(id, lesson);

    SolrNestedComponentMapper componentMapper
        = new SolrNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}