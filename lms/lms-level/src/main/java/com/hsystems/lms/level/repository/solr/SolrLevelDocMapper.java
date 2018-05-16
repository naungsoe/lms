package com.hsystems.lms.level.repository.solr;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;

import org.apache.solr.common.SolrInputDocument;

public final class SolrLevelDocMapper
    implements Mapper<Auditable<Level>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String NAME_FIELD = "name";

  public SolrLevelDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<Level> source) {
    SolrInputDocument document = new SolrInputDocument();
    Level level = source.getEntity();
    document.addField(ID_FIELD, level.getId());

    String typeName = Level.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);
    document.addField(NAME_FIELD, level.getName());

    String parentId = level.getId();
    School school = level.getSchool();

    if (school != null) {
      SolrSchoolRefDocMapper schoolRefDocMapper
          = new SolrSchoolRefDocMapper(parentId);
      SolrInputDocument schoolDocument
          = schoolRefDocMapper.from(school);
      document.addChildDocument(schoolDocument);
    }

    SolrAuditableDocMapper<Level> auditableDocMapper
        = new SolrAuditableDocMapper<>(document, parentId);
    return auditableDocMapper.from(source);
  }
}