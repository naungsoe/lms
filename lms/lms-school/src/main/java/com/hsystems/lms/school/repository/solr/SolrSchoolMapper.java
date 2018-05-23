package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrSchoolMapper
    implements Mapper<SolrDocument, Auditable<School>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrSchoolMapper() {

  }

  @Override
  public Auditable<School> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    School.Builder builder = new School.Builder(id, name);

    SolrAuditableMapper<School> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}