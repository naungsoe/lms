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
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String NAME_FIELD = "name";

  public SolrGroupDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<Group> source) {
    SolrInputDocument document = new SolrInputDocument();
    Group group = source.getEntity();
    document.addField(ID_FIELD, group.getId());

    String typeName = Group.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);
    document.addField(NAME_FIELD, group.getName());

    String parentId = group.getId();
    School school = group.getSchool();

    if (school != null) {
      SolrSchoolRefDocMapper schoolRefDocMapper
          = new SolrSchoolRefDocMapper(parentId);
      SolrInputDocument schoolDocument
          = schoolRefDocMapper.from(school);
      document.addChildDocument(schoolDocument);
    }

    SolrAuditableDocMapper<Group> auditableDocMapper
        = new SolrAuditableDocMapper<>(document, parentId);
    return auditableDocMapper.from(source);
  }
}