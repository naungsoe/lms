package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrSchoolMapper
    implements Mapper<SolrDocument, Auditable<School>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";
  private static final String PERMISSIONS_FIELD = "permissions";

  private final SolrPreferencesMapper preferencesMapper;

  public SolrSchoolMapper() {
    this.preferencesMapper = new SolrPreferencesMapper();
  }

  @Override
  public Auditable<School> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    List<String> permissions
        = SolrUtils.getStrings(source, PERMISSIONS_FIELD);
    Preferences preferences = preferencesMapper.from(source);
    School school = new School.Builder(id, name)
        .permissions(permissions)
        .preferences(preferences)
        .build();

    SolrAuditableMapper<School> auditableMapper
        = new SolrAuditableMapper<>(school, id);
    return auditableMapper.from(source);
  }
}