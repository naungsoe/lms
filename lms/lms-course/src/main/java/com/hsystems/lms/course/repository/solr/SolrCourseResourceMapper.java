package com.hsystems.lms.course.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.course.repository.entity.Course;
import com.hsystems.lms.course.repository.entity.CourseResource;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrCourseResourceMapper
    implements Mapper<SolrDocument, Auditable<CourseResource>> {

  private static final String ID_FIELD = "id";

  private final List<Component> components;

  public SolrCourseResourceMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Auditable<CourseResource> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    SolrCourseMapper courseMapper = new SolrCourseMapper(components);
    Course course = courseMapper.from(source);
    CourseResource.Builder builder = new CourseResource.Builder(id, course);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrAuditableMapper<CourseResource> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}