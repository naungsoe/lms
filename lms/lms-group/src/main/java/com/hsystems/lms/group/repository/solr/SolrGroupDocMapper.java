package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;

import org.apache.solr.common.SolrInputDocument;

public final class SolrGroupDocMapper
    implements Mapper<Auditable<Group>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrGroupDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<Group> source) {
    SolrInputDocument document = new SolrInputDocument();
    Group group = source.getEntity();
    document.addField(ID_FIELD, group.getId());
    document.addField(NAME_FIELD, group.getName());

    School school = group.getSchool();
    SolrSchoolRefDocMapper schoolRefDocMapper
        = new SolrSchoolRefDocMapper(document);
    document = schoolRefDocMapper.from(school);

    SolrAuditableDocMapper<Group> auditableDocMapper
        = new SolrAuditableDocMapper<>(document);
    return auditableDocMapper.from(source);
  }
}