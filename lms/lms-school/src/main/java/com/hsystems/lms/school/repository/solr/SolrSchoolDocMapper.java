package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.solr.common.SolrInputDocument;

public final class SolrSchoolDocMapper
    implements Mapper<Auditable<School>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrSchoolDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<School> source) {
    SolrInputDocument document = new SolrInputDocument();
    School school = source.getEntity();
    document.addField(ID_FIELD, school.getId());
    document.addField(NAME_FIELD, school.getName());

    SolrAuditableDocMapper<School> auditableDocMapper
        = new SolrAuditableDocMapper<>(document);
    return auditableDocMapper.from(source);
  }
}