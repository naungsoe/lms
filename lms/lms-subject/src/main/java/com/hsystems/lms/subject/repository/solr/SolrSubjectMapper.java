package com.hsystems.lms.subject.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.solr.common.SolrDocument;

public final class SolrSubjectMapper
    implements Mapper<SolrDocument, Auditable<Subject>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrSubjectMapper() {

  }

  @Override
  public Auditable<Subject> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    Subject.Builder builder = new Subject.Builder(id, name);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrAuditableMapper<Subject> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}