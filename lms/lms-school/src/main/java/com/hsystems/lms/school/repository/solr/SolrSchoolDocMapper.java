package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.solr.common.SolrInputDocument;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public final class SolrSchoolDocMapper
    implements Mapper<Auditable<School>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String NAME_FIELD = "name";
  private static final String PERMISSIONS_FIELD = "permissions";

  private final SolrPreferencesMapMapper preferencesMapper;

  public SolrSchoolDocMapper() {
    this.preferencesMapper = new SolrPreferencesMapMapper();
  }

  @Override
  public SolrInputDocument from(Auditable<School> source) {
    SolrInputDocument document = new SolrInputDocument();
    School school = source.getEntity();
    document.addField(ID_FIELD, school.getId());

    String typeName = School.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);
    document.addField(NAME_FIELD, school.getName());

    Enumeration<String> enumeration = school.getPermissions();
    document.addField(PERMISSIONS_FIELD, Collections.list(enumeration));

    Preferences preferences = school.getPreferences();
    Map<String, Object> preferencesMap = preferencesMapper.from(preferences);
    preferencesMap.forEach((key, value) -> document.addField(key, value));

    String parentId = school.getId();
    SolrAuditableDocMapper<School> auditableDocMapper
        = new SolrAuditableDocMapper<>(document, parentId);
    return auditableDocMapper.from(source);
  }
}