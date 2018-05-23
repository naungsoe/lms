package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrSchoolRefMapper
    implements Mapper<SolrDocument, School> {

  private static final String ID_FIELD = "school.id";
  private static final String NAME_FIELD = "school.name";

  public SolrSchoolRefMapper() {

  }

  @Override
  public School from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    return new School.Builder(id, name).build();
  }
}