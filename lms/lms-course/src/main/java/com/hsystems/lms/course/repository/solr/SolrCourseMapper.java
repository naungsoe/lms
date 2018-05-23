package com.hsystems.lms.course.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.course.repository.entity.Course;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrCourseMapper implements Mapper<SolrDocument, Course> {

  private static final String TITLE_FIELD = "title";
  private static final String DESCRIPTION_FIELD = "description";

  private final List<Component> components;

  public SolrCourseMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Course from(SolrDocument source) {
    String title = SolrUtils.getString(source, TITLE_FIELD);
    String description = SolrUtils.getString(source, DESCRIPTION_FIELD);
    return new Course(title, description, components);
  }
}