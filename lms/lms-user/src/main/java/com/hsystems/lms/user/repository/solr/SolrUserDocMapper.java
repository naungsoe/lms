package com.hsystems.lms.user.repository.solr;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.solr.SolrGroupRefDocsMapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrPreferencesMapMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import org.apache.solr.common.SolrInputDocument;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public final class SolrUserDocMapper
    implements Mapper<Auditable<AppUser>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";
  private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
  private static final String GENDER_FIELD = "gender";
  private static final String MOBILE_FIELD = "mobile";
  private static final String EMAIL_FIELD = "email";
  private static final String PERMISSIONS_FIELD = "permissions";
  private static final String MFA_ENABLED_FIELD = "mfaEnabled";

  private final SolrPreferencesMapMapper preferencesMapper;

  public SolrUserDocMapper() {
    this.preferencesMapper = new SolrPreferencesMapMapper();
  }

  @Override
  public SolrInputDocument from(Auditable<AppUser> source) {
    SolrInputDocument document = new SolrInputDocument();
    AppUser user = source.getEntity();
    document.addField(ID_FIELD, user.getId());

    String typeName = User.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);
    document.addField(FIRST_NAME_FIELD, user.getFirstName());
    document.addField(LAST_NAME_FIELD, user.getLastName());
    document.addField(DATE_OF_BIRTH_FIELD, user.getDateOfBirth());
    document.addField(GENDER_FIELD, user.getGender());
    document.addField(MOBILE_FIELD, user.getMobile());
    document.addField(EMAIL_FIELD, user.getEmail());

    Enumeration<String> enumeration = user.getPermissions();
    document.addField(PERMISSIONS_FIELD, Collections.list(enumeration));
    document.addField(MFA_ENABLED_FIELD, user.isMfaEnabled());

    Preferences preferences = source.getEntity().getPreferences();
    Map<String, Object> preferencesMap = preferencesMapper.from(preferences);
    preferencesMap.forEach((key, value) -> document.addField(key, value));

    String parentId = user.getId();

    if (user instanceof SchoolUser) {
      SchoolUser schoolUser = (SchoolUser) user;
      School school = schoolUser.getSchool();

      if (school != null) {
        SolrSchoolRefDocMapper schoolRefDocMapper
            = new SolrSchoolRefDocMapper(parentId);
        SolrInputDocument schoolDocument
            = schoolRefDocMapper.from(school);
        document.addChildDocument(schoolDocument);
      }

      List<Group> groups = Collections.list(schoolUser.getGroups());

      if (CollectionUtils.isNotEmpty(groups)) {
        SolrGroupRefDocsMapper groupRefDocsMapper
            = new SolrGroupRefDocsMapper(parentId);
        List<SolrInputDocument> groupDocuments
            = groupRefDocsMapper.from(groups);
        document.addChildDocuments(groupDocuments);
      }
    }

    SolrAuditableDocMapper<AppUser> auditableDocMapper
        = new SolrAuditableDocMapper<>(document, parentId);
    return auditableDocMapper.from(source);
  }
}