package com.hsystems.lms.subject.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.solr.common.SolrInputDocument;

public final class SolrSubjectDocMapper
    implements Mapper<Auditable<Subject>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String NAME_FIELD = "name";

  public SolrSubjectDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<Subject> source) {
    SolrInputDocument document = new SolrInputDocument();
    Subject subject = source.getEntity();
    document.addField(ID_FIELD, subject.getId());

    String typeName = Subject.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);
    document.addField(NAME_FIELD, subject.getName());

    School school = subject.getSchool();
    SolrSchoolRefDocMapper schoolRefDocMapper
        = new SolrSchoolRefDocMapper(document);
    document = schoolRefDocMapper.from(school);

    SolrAuditableDocMapper<Subject> auditableDocMapper
        = new SolrAuditableDocMapper<>(document);
    return auditableDocMapper.from(source);
  }
}