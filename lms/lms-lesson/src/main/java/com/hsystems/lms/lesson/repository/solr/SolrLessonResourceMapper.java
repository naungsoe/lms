package com.hsystems.lms.lesson.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.lesson.repository.entity.Lesson;
import com.hsystems.lms.lesson.repository.entity.LessonResource;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrLessonResourceMapper
    implements Mapper<SolrDocument, Auditable<LessonResource>> {

  private static final String ID_FIELD = "id";

  private final List<Component> components;

  public SolrLessonResourceMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Auditable<LessonResource> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    SolrLessonMapper lessonMapper = new SolrLessonMapper(components);
    Lesson lesson = lessonMapper.from(source);
    LessonResource.Builder builder = new LessonResource.Builder(id, lesson);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrAuditableMapper<LessonResource> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}